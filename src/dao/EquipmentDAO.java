package dao;

import model.Equipment;
import util.JDBCConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    public boolean addEquipment(Equipment eq) {
        String sql = "INSERT INTO equipments (name, total_quantity, available_quantity, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eq.getName());
            stmt.setInt(2, eq.getTotalQuantity());
            stmt.setInt(3, eq.getAvailableQuantity());
            stmt.setString(4, eq.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm thiết bị: " + e.getMessage());
            return false;
        }
    }

    public List<Equipment> getAllEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM equipments";
        try (Connection conn = JDBCConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Equipment eq = new Equipment();
                eq.setId(rs.getInt("id"));
                eq.setName(rs.getString("name"));
                eq.setTotalQuantity(rs.getInt("total_quantity"));
                eq.setAvailableQuantity(rs.getInt("available_quantity"));
                eq.setStatus(rs.getString("status"));
                equipmentList.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách thiết bị: " + e.getMessage());
        }
        return equipmentList;
    }

    public boolean updateAvailableQuantity(int id, int addedQuantity) {
        String sql = "UPDATE equipments SET available_quantity = available_quantity + ?, total_quantity = total_quantity + ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, addedQuantity);
            stmt.setInt(2, addedQuantity);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật số lượng thiết bị: " + e.getMessage());
            return false;
        }
    }
}
