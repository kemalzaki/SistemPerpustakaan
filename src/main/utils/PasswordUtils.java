package main.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LEN = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LEN = 256;

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LEN];
        RANDOM.nextBytes(salt);
        return bytesToHex(salt);
    }

    public static String hash(char[] password, String saltHex) {
        try {
            byte[] salt = hexToBytes(saltHex);
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LEN);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(char[] password, String saltHex, String expectedHashHex) {
        String h = hash(password, saltHex);
        return h.equals(expectedHashHex);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static byte[] hexToBytes(String hex) {
        if (hex == null || hex.isEmpty()) return new byte[0];
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}
