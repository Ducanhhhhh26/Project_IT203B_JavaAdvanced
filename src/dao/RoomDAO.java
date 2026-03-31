package dao;

import model.Room;
import util.JDBCConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms (name, capacity, location, equipment_desc, price_per_hour) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getCapacity());
            stmt.setString(3, room.getLocation());
            stmt.setString(4, room.getEquipmentDesc());
            stmt.setDouble(5, room.getPricePerHour());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm phòng: " + e.getMessage());
            return false;
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = JDBCConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách phòng: " + e.getMessage());
        }
        return rooms;
    }

    public List<Room> getAvailableRooms(Timestamp start, Timestamp end, int minCapacity) {
        List<Room> rooms = new ArrayList<>();
        // Phòng trống là phòng không có booking PENDING/APPROVED cắt ngang thời gian
        String sql = "SELECT * FROM rooms r WHERE r.capacity >= ? AND r.id NOT IN ("
                   + "SELECT room_id FROM bookings WHERE status IN ('PENDING', 'APPROVED') "
                   + "AND ((start_time < ? AND end_time > ?) "
                   + "OR (start_time >= ? AND start_time < ?)))";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, minCapacity);
            stmt.setTimestamp(2, end);
            stmt.setTimestamp(3, start);
            stmt.setTimestamp(4, start);
            stmt.setTimestamp(5, end);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách phòng trống: " + e.getMessage());
        }
        return rooms;
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET name = ?, capacity = ?, location = ?, equipment_desc = ?, price_per_hour = ? WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getCapacity());
            stmt.setString(3, room.getLocation());
            stmt.setString(4, room.getEquipmentDesc());
            stmt.setDouble(5, room.getPricePerHour());
            stmt.setInt(6, room.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật phòng: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRoom(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa phòng: " + e.getMessage());
            return false;
        }
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getInt("id"));
        room.setName(rs.getString("name"));
        room.setCapacity(rs.getInt("capacity"));
        room.setLocation(rs.getString("location"));
        room.setEquipmentDesc(rs.getString("equipment_desc"));
        room.setPricePerHour(rs.getDouble("price_per_hour"));
        return room;
    }
}
