package app.service.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

public class AESCryptographer {
    private static final int IV_LENGTH=16;
    private static final String ALGORITHM_PADDING = "/CBC/PKCS5Padding";
    private static final int ALGORITHM_KEY_SIZE = 256;

    SecretKey secretKey;

    // Load with key
    public AESCryptographer(SecretKey key) {
        this.secretKey = key;
    }
    // Load Key from path
    public AESCryptographer(String keyPath) throws IOException {
        this.secretKey = loadKey(keyPath);
    }
    public AESCryptographer(byte[] keyBytes) {
        secretKey = new SecretKeySpec(keyBytes, "AES");
    }
    // Generate New key
    public AESCryptographer() {
        this.secretKey = generateKey();
    }
    // Create key from bytes() ?

    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(ALGORITHM_KEY_SIZE);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    private SecretKey loadKey(String filePath) throws IOException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        return new SecretKeySpec(keyBytes, "AES");
    }
    public void saveKey(String filePath) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            byte[] keyBytes = secretKey.getEncoded();
            out.write(keyBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public SecretKey getKey() { return secretKey; }

    public IvParameterSpec generateIVSpec() {
        // Generating the Initialization Vector
        byte[] iv = new byte[IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    public Cipher generateCipher(int type, IvParameterSpec ivSpec) {
        try {
            Cipher ci = Cipher.getInstance( secretKey.getAlgorithm() + ALGORITHM_PADDING);
            ci.init(type, secretKey, ivSpec);
            return ci;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void encryptAndSerialize(Serializable object, OutputStream ostream) throws IOException, IllegalBlockSizeException {
        IvParameterSpec ivSpec = generateIVSpec();
        Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE, ivSpec);

        SealedObject sealedObject = new SealedObject(object, cipher);

        ostream.write(ivSpec.getIV());
        ostream.flush();

        CipherOutputStream cos = new CipherOutputStream(ostream, cipher);
        ObjectOutputStream outputStream = new ObjectOutputStream(cos);
        outputStream.writeObject(sealedObject);
        outputStream.close();
    }

    public Object decryptAndDeserialize(InputStream istream) throws IOException {
        byte[] iv = new byte[IV_LENGTH];
        istream.read(iv);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = generateCipher(Cipher.DECRYPT_MODE, ivSpec);

        CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
        ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
        SealedObject sealedObject;
        try {
            sealedObject = (SealedObject) inputStream.readObject();
            inputStream.close();
            return sealedObject.getObject(cipher);
        } catch (ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
