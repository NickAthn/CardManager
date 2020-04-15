package app.util;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

public class CryptoUtils {
    /**
     * Generate RSA key pair with given size.
     *
     * @return KeyPair
     * @param keysize this is the key size of the RSA keys. Recommended value is 2048.
     */
    public static KeyPair generateRSAKeyPair(int keysize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keysize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Generate AES secret key with given size.
     *
     * @return SecretKey
     * @param keysize this is the key size of the AES key. Recommended value is 256.
     */
    public static SecretKey generateAESKey(int keysize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keysize);
        return keyGenerator.generateKey();
    }

    /**
     * Reads an RSA public key in the X.509 format
     *
     * @return PublicKey
     * @param filename this is the full path of the key. ex: /Data/Keys/key_name_here.key
     */
    public static PublicKey readPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(FileUtils.readData(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }
    /**
     * Reads an RSA private key in the PKCS#8 format
     *
     * @return PrivateKey
     * @param filename this is the full path of the key. ex: /Data/Keys/key_name_here.key
     */
    public static PrivateKey readPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(FileUtils.readData(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    /**
     * Reads an AES secret key
     *
     * @return SecretKey
     * @param filename this is the full path of the key. ex: /Data/Keys/key_name_here.key
     */
    public static SecretKey readSecretKey(String filename, String filename1) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        //SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        //KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        //SecretKey tmp = factory.generateSecret(spec);
        //SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        PrivateKey key = readPrivateKey(filename1);
        byte[] data = FileUtils.readData(filename);
        byte[] keyb = decrypt(key, data);
        SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
        return skey;
    }


    

    public static byte[] encrypt(PublicKey key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    public static byte[] decrypt(PrivateKey key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    public static byte[] encrypt(SecretKey key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    public static byte[] decrypt(SecretKey key, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }




}
