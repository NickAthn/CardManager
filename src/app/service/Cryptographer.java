package app.service;
import app.util.FileUtils;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;


public class Cryptographer {
    Storage storage = new Storage();
    public static String privateKeyName = "app_rsa_private.der";
    public String publicKeyName = "app_rsa.der";


    public Cryptographer() {
        // Check if RSA keypair is valid
        if (!validateKeyPair()) {
            // If its not valid generate a new one.
            try {
                generateRSAKeyPair();
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createKeyForUser(String username) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        SecretKey secretKey = generateAESKey();
        // Getting the applications public key
        PublicKey publicKey = readPublicKey(Storage.keysDir + publicKeyName);
        // Encrypting the key
        byte[] encryptedPrivateKey = encrypt(publicKey,secretKey.getEncoded());
        // Saving the key
        FileUtils.saveData(encryptedPrivateKey, Storage.keysDir + username + "_encrypted_secret.key");
    }

    public PublicKey readPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(FileUtils.readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    public static PrivateKey readPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(FileUtils.readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    public byte[] encrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        PublicKey key = readPublicKey(Storage.keysDir + publicKeyName);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    public byte[] encrypt(PublicKey key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    public static byte[] decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        PrivateKey privateKey = readPrivateKey(Storage.keysDir + privateKeyName);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public byte[] decrypt(PrivateKey key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public void generateRSAKeyPair() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        Key pvt = kp.getPrivate();

        storage.saveKey(pvt,privateKeyName);
        storage.saveKey(pub,publicKeyName);
    }

    public SecretKey generateAESKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey secretKey = kgen.generateKey();
        return secretKey;
    }

    public boolean validateKeyPair() {
        try {
            PublicKey publicKey = readPublicKey(Storage.keysDir + publicKeyName);
            PrivateKey privateKey = readPrivateKey(Storage.keysDir +privateKeyName);
            // create a challenge
            byte[] challenge = new byte[10000];
            ThreadLocalRandom.current().nextBytes(challenge);
            // sign using the private key
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privateKey);
            sig.update(challenge);
            byte[] signature = sig.sign();
            // verify signature using the public key
            sig.initVerify(publicKey);
            sig.update(challenge);
            return sig.verify(signature);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Hash a password for storage.
     * Password hashing is done using PBKDF2 as it still considered a more robust option over older (SHA-512, MD5) implementations
     *
     * @return a secure authentication token to be stored for later authentication. Contains number of iterations and the salt used
     */
    public String hash(char[] password) {
        try {
            byte[] salt = getSalt();
            int iterations = 65536;

            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 64 * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Authenticate with a password and a stored password token.
     *
     * @return true if the password and token match
     */
    public static boolean validatePassword(char[] originalPassword, String storedPassword) {
        try {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(originalPassword, salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generates salt
     *
     * @return a salt to be used in password hashing
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Converts a byte array to a Hex string. Useful for storage
     *
     * @return String
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * Converts a Hex string to a byte array. Useful for authentication check
     *
     * @return byte[]
     */
    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
