package service;

import dao.BookingDAO;
import dao.BookingDetailDAO;
import model.Booking;
import model.BookingDetail;
import java.sql.Timestamp;
import java.util.List;
import dao.RoomDAO;
import dao.EquipmentDAO;
import model.Room;
import model.Equipment;
import util.Logger;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();
    private BookingDetailDAO bookingDetailDAO = new BookingDetailDAO();
    private RoomDAO roomDAO = new RoomDAO();
    private EquipmentDAO equipmentDAO = new EquipmentDAO();

    public boolean bookRoom(int userId, int roomId, Timestamp start, Timestamp end, List<BookingDetail> details) {
        // Kiểm tra xung đột thời gian (Booking Logic)
        if (!bookingDAO.checkRoomAvailable(roomId, start, end)) {
            Logger.warning("Phòng ID " + roomId + " đang được đặt trong khung giờ yêu cầu.");
            return false;
        }

        // Tính số giờ thuê
        long diffMillis = end.getTime() - start.getTime();
        double hours = (double) diffMillis / (1000 * 60 * 60);

        // Lấy thông tin phòng để tính chi phí thuê phòng
        double roomPrice = 0;
        for (Room r : roomDAO.getAllRooms()) {
            if (r.getId() == roomId) {
                roomPrice = r.getPricePerHour();
                break;
            }
        }
        double roomCost = hours * roomPrice;

        // Tính chi phí thiết bị
        double equipmentCost = 0;
        if (details != null && !details.isEmpty()) {
            List<Equipment> allEqs = equipmentDAO.getAllEquipment();
            for (BookingDetail d : details) {
                for (Equipment eq : allEqs) {
                    if (eq.getId() == d.getItemId()) {
                        equipmentCost += eq.getRentalPrice() * d.getQuantity();
                        break;
                    }
                }
            }
        }

        double totalCost = roomCost + equipmentCost;

        // Tạo Booking trạng thái PENDING
        Booking b = new Booking();
        b.setUserId(userId);
        b.setRoomId(roomId);
        b.setStartTime(start);
        b.setEndTime(end);
        b.setStatus("PENDING");
        b.setPrepStatus("NOT_READY");
        b.setTotalCost(totalCost);

        int bookingId = bookingDAO.createBooking(b);
        if (bookingId > 0) {
            // Add details
            if (details != null && !details.isEmpty()) {
                bookingDetailDAO.addBookingDetails(bookingId, details);
            }
            Logger.info("Người dùng (ID: " + userId + ") đã đặt phòng thành công (Booking ID: " + bookingId + ")");
            return true;
        }

        Logger.error("Lỗi khi tạo yêu cầu đặt phòng cho người dùng (ID: " + userId + ")");
        return false;
    }

    public List<Booking> getMyBookings(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    public boolean addFeedback(int bookingId, int rating, String feedback) {
        if (rating < 1 || rating > 5) {
            System.out.println("Lỗi: Đánh giá phải từ 1 đến 5 sao.");
            return false;
        }
        boolean success = bookingDAO.updateFeedback(bookingId, rating, feedback);
        if (success) {
            Logger.info("Đã nhận được đánh giá " + rating + " sao cho cuộc họp (Booking ID: " + bookingId + ")");
        }
        return success;
    }
}
