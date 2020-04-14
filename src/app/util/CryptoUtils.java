package app.util;

import javax.crypto.*;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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




}