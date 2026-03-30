package service;

import dao.RoomDAO;
import model.Room;
import java.util.List;

public class RoomService {
    private RoomDAO roomDAO = new RoomDAO();

    public boolean createRoom(String name, int capacity, String location, String equipmentDesc) {
        if (capacity <= 0) {
            System.err.println("Sức chứa phải lớn hơn 0.");
            return false;
        }
        Room room = new Room();
        room.setName(name);
        room.setCapacity(capacity);
        room.setLocation(location);
        room.setEquipmentDesc(equipmentDesc);
        return roomDAO.addRoom(room);
    }

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public List<model.Room> getAvailableRooms(java.sql.Timestamp start, java.sql.Timestamp end, int minCapacity) {
        return roomDAO.getAvailableRooms(start, end, minCapacity);
    }

    public boolean updateRoom(int id, String name, int capacity, String location, String equipmentDesc) {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setCapacity(capacity);
        room.setLocation(location);
        room.setEquipmentDesc(equipmentDesc);
        return roomDAO.updateRoom(room);
    }

    public boolean deleteRoom(int id) {
        return roomDAO.deleteRoom(id);
    }
}
