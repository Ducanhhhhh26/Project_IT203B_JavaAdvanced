package service;

import dao.BookingDAO;
import model.Booking;
import java.util.List;

public class SupportStaffService {
    private BookingDAO bookingDAO = new BookingDAO();

    public List<Booking> getAssignedTasks(int staffId) {
        return bookingDAO.getBookingsBySupportStaffId(staffId);
    }

    public boolean updatePrepStatus(int bookingId, String status) {
        return bookingDAO.updateBookingPrepStatus(bookingId, status);
    }
}
