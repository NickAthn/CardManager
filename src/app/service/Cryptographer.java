package app.service;
import app.util.FileUtils;
import app.util.PasswordUtils;
import app.util.CryptoUtils;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;



public class Cryptographer {
    Storage storage = new Storage();
    public static String privateKeyName = "app_rsa_private.der";
    public static String publicKeyName = "app_rsa.der";


    public Cryptographer() {
        // Check if RSA keypair is valid
        if (!validateKeyPair()) {
            // If its not valid generate a new one.
            try {
                createAppKeyPair();
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createKeyForUser(String username) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        SecretKey secretKey = CryptoUtils.generateAESKey(256);
        byte[] encryptedPrivateKey = encrypt(secretKey.getEncoded());
        FileUtils.saveData(encryptedPrivateKey, Storage.keysDir + username + "_encrypted_secret.key");
    }

    public byte[] encrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        PublicKey key = CryptoUtils.readPublicKey(Storage.keysDir + publicKeyName);
        return CryptoUtils.encrypt(key, data);
    }
    public static byte[] decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        PrivateKey key = CryptoUtils.readPrivateKey(Storage.keysDir + privateKeyName);
        return CryptoUtils.decrypt(key, data);
    }
    public byte[] encrypt_aes(String username, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        SecretKey key = CryptoUtils.readSecretKey(Storage.keysDir + username + "_encrypted_secret.key" , Storage.keysDir + privateKeyName);
        return CryptoUtils.encrypt(key, data);
    }


    public void createAppKeyPair() throws IOException, NoSuchAlgorithmException {
        KeyPair keyPair = CryptoUtils.generateRSAKeyPair(2048);
        storage.saveKey(keyPair.getPublic(), publicKeyName);
        storage.saveKey(keyPair.getPrivate(), privateKeyName);
    }

    public boolean validateKeyPair() {
        try {
            PublicKey publicKey = CryptoUtils.readPublicKey(Storage.keysDir + publicKeyName);
            PrivateKey privateKey = CryptoUtils.readPrivateKey(Storage.keysDir +privateKeyName);
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
     * Authenticate with a password and a stored password token.
     *
     * @return true if the password and token match
     */
    public static boolean validatePassword(char[] originalPassword, String storedPassword) {
        try {
            String decryptedHashedPassword = new String(decrypt(Base64.getDecoder().decode(storedPassword)), StandardCharsets.UTF_8);
            return PasswordUtils.validatePassword(originalPassword, decryptedHashedPassword);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | NoSuchPaddingException | IOException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return false;
    }

}
