package dao;

import model.User;
import model.Role;
import util.JDBCConnection;
import util.PasswordHash;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password_hash");
                    if (PasswordHash.checkPassword(password, hash)) {
                        return mapResultSetToUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi xác thực: " + e.getMessage());
        }
        return null;
    }

    public boolean registerEmployee(User user) {
        user.setRole(Role.EMPLOYEE);
        return insertUser(user);
    }

    public boolean createSupportStaff(User user) {
        user.setRole(Role.SUPPORT_STAFF);
        return insertUser(user);
    }

    private boolean insertUser(User user) {
        String sql = "INSERT INTO users (username, password_hash, role, full_name, email, phone, department) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, PasswordHash.hashPassword(user.getPassword_hash()));
            stmt.setString(3, user.getRole().name()); // Lưu Enum dạng String vào DB
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.getDepartment());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm người dùng: " + e.getMessage());
        }
        return false;
    }

    public boolean checkUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra username: " + e.getMessage());
        }
        return false;
    }

    public List<User> getUsersByRole(Role role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword_hash(rs.getString("password_hash"));
        user.setRole(Role.valueOf(rs.getString("role"))); // Chuyển chuỗi từ DB sang Enum
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));

        // Handle columns that might not exist in old schema but exist in model
        try {
            user.setPhone(rs.getString("phone"));
        } catch (SQLException e) {
            // ignore if column doesn't exist
        }
        try {
            user.setDepartment(rs.getString("department"));
        } catch (SQLException e) {
             // ignore
        }
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
