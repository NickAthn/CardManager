package app.service.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class RSACryptographer {
    public static final int USE_PRIVATE_KEY = 0;
    public static final int USE_PUBLIC_KEY = 1;


    private static final String ALGORITHM = "RSA";
    private static final String ALGORITHM_PADDING = "/ECB/PKCS1Padding";
    private static final int ALGORITHM_KEY_SIZE = 2048;
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    // Generate new key
    public RSACryptographer() {
        KeyPair keyPair = generateKeyPair();
        assert keyPair != null;
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }
    // Init using keyPair
    public RSACryptographer(KeyPair keyPair) {
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }



    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
            kpg.initialize(ALGORITHM_KEY_SIZE);
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void savePrivateKey(String filePath) throws IOException {
        Files.write(new File(filePath).toPath(),privateKey.getEncoded());
    }
    public void savePublicKey(String filePath) throws IOException {
        Files.write(new File(filePath).toPath(),publicKey.getEncoded());
    }

    public static PrivateKey readPrivateKey(String filePath) throws InvalidKeySpecException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static PublicKey readPublicKey(String filePath) throws InvalidKeySpecException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] encrypt(byte[] bytes, int keyMode) {
        Key key = null;
        if (keyMode == USE_PRIVATE_KEY) key = privateKey;
        if (keyMode == USE_PUBLIC_KEY) key = publicKey;
        assert key != null;

        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm() + ALGORITHM_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(bytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(byte[] bytes, int keyMode) {
        Key key = null;
        if (keyMode == USE_PRIVATE_KEY) key = privateKey;
        if (keyMode == USE_PUBLIC_KEY) key = publicKey;
        assert key != null;

        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm() + ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(bytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String sign(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
        privateSignature.initSign(privateKey);
        privateSignature.update(bytes);
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }
    public boolean verify(byte[] bytes, String signature) throws Exception {
        Signature publicSignature = Signature.getInstance(SIGNATURE_ALGORITHM);
        publicSignature.initVerify(publicKey);
        publicSignature.update(bytes);

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    public static boolean validateKeyPair(KeyPair pair) {
        try {
            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();
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
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return false;
        }
    }

}
