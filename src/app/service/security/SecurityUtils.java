package app.service.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurityUtils {
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

    public static String hash(byte[] bytes) throws NoSuchAlgorithmException {
//        byte[] salt = getSalt();
        final MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(salt);
        final byte[] hashedBytes = md.digest(bytes);
        return bytesToHex(hashedBytes);
    }

    public static String getDirectoryChecksum(String dir) throws IOException, NoSuchAlgorithmException {
        String hash = "";
        File folder = new File(dir);
        File[] files = folder.listFiles();

        assert files != null;
        for (File file : files) {
            hash += getFileChecksum(new File(file.toString()));
        }
        hash = GetMD5HashOfString(hash);
        return hash;
    }
    // TODO: Figure out this part
    public static String GetMD5HashOfString  (String str) {
        MessageDigest md5 ;
        StringBuilder hexString = new StringBuilder();
        try {
            md5 = MessageDigest.getInstance("SHA-256");
            md5.reset();
            md5.update(str.getBytes());
            byte[] messageDigest = md5.digest();
            for (byte b : messageDigest) {
                hexString.append(Integer.toHexString((0xF0 & b) >> 4));
                hexString.append(Integer.toHexString(0x0F & b));
            }
        } catch (Throwable e) {e.printStackTrace();}
        return hexString.toString();
    }
    private static String getFileChecksum(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);
        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        //return complete hash
        return sb.toString();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
