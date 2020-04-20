package app.service.security;

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
        final MessageDigest md = MessageDigest.getInstance("SHA3_256");
//        md.update(salt);
        final byte[] hashedBytes = md.digest(bytes);
        return bytesToHex(hashedBytes);
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
