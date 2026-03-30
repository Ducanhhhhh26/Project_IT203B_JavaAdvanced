package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHash {

    /**
     * Mã hóa mật khẩu sử dụng thuật toán SHA-256
     * @param password Mật khẩu gốc
     * @return Chuỗi mật khẩu đã được mã hóa (Base64)
     */
    public static String hashPassword(String password) {
        if (password == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không tìm thấy thuật toán mã hóa", e);
        }
    }

    /**
     * Kiểm tra mật khẩu gốc có khớp với mật khẩu đã mã hóa hay không
     * @param plainPassword Mật khẩu gốc (người dùng nhập)
     * @param hashedPassword Mật khẩu đã mã hóa (lưu trong DB)
     * @return true nếu khớp, false nếu không khớp
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        String newHash = hashPassword(plainPassword);
        return newHash.equals(hashedPassword);
    }
}
