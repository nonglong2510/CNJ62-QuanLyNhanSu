package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 310_000;
    private static final int MAX_VERIFY_ITERATIONS = 1_000_000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Mật khẩu không được null.");
        }

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] hash = deriveKey(password.toCharArray(), salt, ITERATIONS);
        return PREFIX + "$" + ITERATIONS + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(salt) + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String storedPassword) {
        if (password == null || storedPassword == null) {
            return false;
        }

        if (storedPassword.startsWith(PREFIX + "$")) {
            String[] parts = storedPassword.split("\\$", -1);
            if (parts.length != 4) {
                return false;
            }
            try {
                int iterations = Integer.parseInt(parts[1]);
                if (iterations < 1 || iterations > MAX_VERIFY_ITERATIONS) {
                    return false;
                }
                byte[] salt = Base64.getDecoder().decode(parts[2]);
                byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
                if (salt.length != SALT_LENGTH || expectedHash.length != KEY_LENGTH / 8) {
                    return false;
                }
                byte[] actualHash = deriveKey(password.toCharArray(), salt, iterations);
                return MessageDigest.isEqual(expectedHash, actualHash);
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }

        if (storedPassword.matches("(?i)[0-9a-f]{64}")) {
            return MessageDigest.isEqual(
                    storedPassword.toLowerCase().getBytes(java.nio.charset.StandardCharsets.US_ASCII),
                    hashLegacySha256(password).getBytes(java.nio.charset.StandardCharsets.US_ASCII));
        }
        return password.equals(storedPassword);
    }

    public static boolean needsUpgrade(String storedPassword) {
        return storedPassword == null || !storedPassword.startsWith(PREFIX + "$");
    }

    private static byte[] deriveKey(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (java.security.GeneralSecurityException ex) {
            throw new IllegalStateException("Không thể tạo mã xác thực mật khẩu.", ex);
        } finally {
            spec.clearPassword();
            java.util.Arrays.fill(password, '\0');
        }
    }

    private static String hashLegacySha256(String password) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte value : hash) {
                hex.append(String.format("%02x", value & 0xff));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Không tìm thấy thuật toán SHA-256.", ex);
        }
    }
}
