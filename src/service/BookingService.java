package service;

import dao.BookingDAO;
import dao.BookingDetailDAO;
import model.Booking;
import model.BookingDetail;
import java.sql.Timestamp;
import java.util.List;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();
    private BookingDetailDAO bookingDetailDAO = new BookingDetailDAO();

    public boolean bookRoom(int userId, int roomId, Timestamp start, Timestamp end, List<BookingDetail> details) {
        // Kiểm tra xung đột thời gian (Booking Logic)
        if (!bookingDAO.checkRoomAvailable(roomId, start, end)) {
            System.out.println("Lỗi: Phòng đang được đặt trong khung giờ này.");
            return false;
        }

        // Tạo Booking trạng thái PENDING
        Booking b = new Booking();
        b.setUserId(userId);
        b.setRoomId(roomId);
        b.setStartTime(start);
        b.setEndTime(end);
        b.setStatus("PENDING");
        b.setPrepStatus("NOT_READY");
        b.setTotalCost(0); // For now

        int bookingId = bookingDAO.createBooking(b);
        if (bookingId > 0) {
            // Add details
            if (details != null && !details.isEmpty()) {
                return bookingDetailDAO.addBookingDetails(bookingId, details);
            }
            return true;
        }

        return false;
    }
}
