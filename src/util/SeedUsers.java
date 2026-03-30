package util;

import dao.UserDAO;
import model.User;
import model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SeedUsers {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        if (!userDAO.checkUsernameExists("admin")) {
            System.out.println("Tạo Admin thủ công...");
            String sql = "INSERT INTO users (username, password_hash, role, full_name, email) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = JDBCConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "admin");
                stmt.setString(2, PasswordHash.hashPassword("admin123"));
                stmt.setString(3, Role.ADMIN.name());
                stmt.setString(4, "Quản Trị Viên");
                stmt.setString(5, "admin@example.com");
                stmt.executeUpdate();
                System.out.println("Đã tạo Admin: username = admin, password = admin123");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Tài khoản admin đã tồn tại.");
        }
    }
}
