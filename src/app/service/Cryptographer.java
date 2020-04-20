package app.service;
import app.util.FileUtils;
import app.util.PasswordUtils;
import app.util.CryptoUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.UUID;
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
    // RSA Encryption using app public key
    public byte[] encrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        PublicKey key = CryptoUtils.readPublicKey(Storage.keysDir + publicKeyName);
        return CryptoUtils.encrypt(key, data);
    }
    // RSA Decryption using app private key
    public static byte[] decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        PrivateKey key = CryptoUtils.readPrivateKey(Storage.keysDir + privateKeyName);
        return CryptoUtils.decrypt(key, data);
    }

    //
    public byte[] sealObject(String username, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        byte[] encryptedSecretKey = FileUtils.readData(Storage.keysDir + username + "_encrypted_secret.key");
        SecretKey secretKey = new SecretKeySpec(decrypt(encryptedSecretKey), "AES");
        return CryptoUtils.encrypt(secretKey, data);
    }
    public SealedObject sealObject(String username, Object obj) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        byte[] encryptedSecretKey = FileUtils.readData(Storage.keysDir + username + "_encrypted_secret.key");
        SecretKey secretKey = new SecretKeySpec(decrypt(encryptedSecretKey), "AES");
        return CryptoUtils.encrypt(secretKey, obj);
    }
    public Object unsealObject(String username, SealedObject obj) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        byte[] encryptedSecretKey = FileUtils.readData(Storage.keysDir + username + "_encrypted_secret.key");
        SecretKey secretKey = new SecretKeySpec(decrypt(encryptedSecretKey), "AES");
        return CryptoUtils.encrypt(secretKey, obj);
    }

    public void saveSealedObject(String username, String path, SealedObject sealedObject) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        byte[] encryptedSecretKey = FileUtils.readData(Storage.keysDir + username + "_encrypted_secret.key");
        SecretKey secretKey = new SecretKeySpec(decrypt(encryptedSecretKey), "AES");

        // generate IV
        SecureRandom random = new SecureRandom();
        byte [] iv = new byte [16];
        random.nextBytes( iv );

        Cipher cipher = Cipher.getInstance( secretKey.getAlgorithm() + "/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec( iv ));

        // Create stream
        FileOutputStream fos = new FileOutputStream(path);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject( sealedObject );
        oos.close();
    }
    public Object readSealedObject(String username, String path) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, ClassNotFoundException {
        byte[] encryptedSecretKey = FileUtils.readData(Storage.keysDir + username + "_encrypted_secret.key");
        SecretKey secretKey = new SecretKeySpec(decrypt(encryptedSecretKey), "AES");

        Cipher cipher = Cipher.getInstance( secretKey.getAlgorithm() + "/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // Create stream
        CipherInputStream cipherInputStream = new CipherInputStream( new BufferedInputStream( new FileInputStream( path ) ), cipher );
        ObjectInputStream inputStream = new ObjectInputStream( cipherInputStream );
        SealedObject sealedObject = (SealedObject) inputStream.readObject();
        return sealedObject.getObject(cipher);
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
