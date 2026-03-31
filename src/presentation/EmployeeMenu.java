package presentation;

import model.User;
import java.util.Scanner;
import service.BookingService;
import service.RoomService;
import service.EquipmentService;
import model.Room;
import model.Equipment;
import model.BookingDetail;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeMenu {
    private User currentUser;
    private Scanner scanner = new Scanner(System.in);
    private BookingService bookingService = new BookingService();
    private RoomService roomService = new RoomService();
    private EquipmentService equipmentService = new EquipmentService();

    public EmployeeMenu(User user) {
        this.currentUser = user;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== MENU NHÂN VIÊN ===");
            System.out.println("Xin chào, " + currentUser.getFullName());
            System.out.println("1. Đặt phòng (Booking)");
            System.out.println("2. Xem lịch sử đặt phòng của tôi");
            System.out.println("3. Đánh giá phòng họp (Feedback)");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleBooking();
                    break;
                case "2":
                    viewMyBookings();
                    break;
                case "3":
                    leaveFeedback();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Tính năng đang phát triển hoặc lựa chọn không hợp lệ!");
            }
        }
    }

    private void leaveFeedback() {
        System.out.println("\n--- ĐÁNH GIÁ PHÒNG HỌP ---");
        List<model.Booking> bookings = bookingService.getMyBookings(currentUser.getId());
        boolean hasCompleted = false;
        long now = System.currentTimeMillis();

        for (model.Booking b : bookings) {
            if ("APPROVED".equals(b.getStatus()) && b.getEndTime().getTime() < now) {
                String ratingStr = (b.getRating() > 0) ? b.getRating() + " Sao" : "Chưa đánh giá";
                System.out.printf("ID: %d | Phòng: %d | Thời gian: %s - %s | Trạng thái: %s\n",
                        b.getId(), b.getRoomId(), b.getStartTime().toString(), b.getEndTime().toString(), ratingStr);
                hasCompleted = true;
            }
        }

        if (!hasCompleted) {
            System.out.println("Chưa có cuộc họp nào đã hoàn thành (hoặc đã được duyệt và kết thúc) để đánh giá.");
            return;
        }

        System.out.print("\nNhập ID booking muốn đánh giá (hoặc 0 để thoát): ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine());
            if (bookingId == 0) return;

            System.out.print("Nhập số sao đánh giá (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine());

            System.out.print("Nhập nhận xét/phản hồi của bạn: ");
            String feedback = scanner.nextLine();

            if (bookingService.addFeedback(bookingId, rating, feedback)) {
                System.out.println("Cảm ơn bạn đã gửi đánh giá thành công!");
            } else {
                System.out.println("Gửi đánh giá thất bại.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID hợp lệ và số sao phải là số.");
        }
    }

    private void viewMyBookings() {
        System.out.println("\n--- LỊCH SỬ ĐẶT PHÒNG CỦA TÔI ---");
        List<model.Booking> myBookings = bookingService.getMyBookings(currentUser.getId());
        if (myBookings.isEmpty()) {
            System.out.println("Bạn chưa có lịch đặt phòng nào.");
            return;
        }

        System.out.printf("%-5s | %-10s | %-20s | %-20s | %-15s | %-15s\n", "ID", "Room ID", "Bắt đầu", "Kết thúc", "Trạng thái", "Chuẩn bị");
        System.out.println("-".repeat(95));
        for (model.Booking b : myBookings) {
            System.out.printf("%-5d | %-10d | %-20s | %-20s | %-15s | %-15s\n",
                    b.getId(), b.getRoomId(), b.getStartTime(), b.getEndTime(), b.getStatus(), b.getPrepStatus());
        }
    }

    private void handleBooking() {
        try {
            System.out.println("\n--- ĐẶT PHÒNG HỌP ---");

            LocalDateTime startDT = null;
            LocalDateTime endDT = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            while (true) {
                try {
                    System.out.print("Nhập thời gian bắt đầu (vd: yyyy-MM-dd HH:mm): ");
                    String startStr = scanner.nextLine();
                    System.out.print("Nhập thời gian kết thúc (vd: yyyy-MM-dd HH:mm): ");
                    String endStr = scanner.nextLine();

                    startDT = LocalDateTime.parse(startStr, formatter);
                    endDT = LocalDateTime.parse(endStr, formatter);

                    if (!startDT.isBefore(endDT)) {
                        System.out.println("Lỗi: Thời gian kết thúc phải sau thời gian bắt đầu!");
                        continue;
                    }
                    if (startDT.isBefore(LocalDateTime.now())) {
                        System.out.println("Lỗi: Không thể đặt phòng trong quá khứ!");
                        continue;
                    }
                    break; // All good
                } catch (DateTimeParseException e) {
                    System.out.println("Lỗi: Định dạng ngày giờ không hợp lệ. Vui lòng thử lại!");
                }
            }

            Timestamp startTime = Timestamp.valueOf(startDT);
            Timestamp endTime = Timestamp.valueOf(endDT);

            int initCapacity = 0;
            while (true) {
                System.out.print("Nhập số người tham gia dự kiến: ");
                try {
                    initCapacity = Integer.parseInt(scanner.nextLine());
                    if (initCapacity <= 0) {
                        System.out.println("Lỗi: Số người tham gia phải lớn hơn 0.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi: Sức chứa phải là một số nguyên!");
                }
            }

            List<Room> availableRooms = roomService.getAvailableRooms(startTime, endTime, initCapacity);
            if (availableRooms.isEmpty()) {
                System.out.println("Không có phòng trống nào phù hợp với yêu cầu của bạn.");
                return;
            }

            System.out.println("\nDanh sách phòng trống thích hợp:");
            System.out.printf("%-5s | %-20s | %-10s | %-20s\n", "ID", "Tên", "Sức chứa", "Vị trí");
            System.out.println("-".repeat(65));
            for (Room r : availableRooms) {
                System.out.printf("%-5d | %-20s | %-10d | %-20s\n",
                                  r.getId(), r.getName(), r.getCapacity(), r.getLocation());
            }

            int roomId = -1;
            while (true) {
                System.out.print("Nhập ID phòng muốn đặt: ");
                try {
                    int rId = Integer.parseInt(scanner.nextLine());
                    boolean valid = availableRooms.stream().anyMatch(r -> r.getId() == rId);
                    if (!valid) {
                         System.out.println("Lỗi: ID phòng không hợp lệ hoặc không có trong danh sách tìm kiếm trên.");
                    } else {
                        roomId = rId;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi: ID phòng phải là một số nguyên!");
                }
            }

            // Allow selecting extra equipment
            List<BookingDetail> details = new ArrayList<>();
            System.out.print("Bạn có muốn mượn thêm thiết bị không? (Y/N): ");
            String yesNo = scanner.nextLine();
            if (yesNo.equalsIgnoreCase("Y")) {
                List<Equipment> configs = equipmentService.getAllEquipment();
                System.out.println("Danh sách thiết bị có thể mượn:");
                System.out.printf("%-5s | %-20s | %-15s\n", "ID", "Tên", "Số lượng sẵn có");
                System.out.println("-".repeat(45));
                for (Equipment eq : configs) {
                   System.out.printf("%-5d | %-20s | %-15d\n", eq.getId(), eq.getName(), eq.getAvailableQuantity());
                }
                while (true) {
                    try {
                        System.out.print("Nhập ID thiết bị để thêm (hoặc 0 để dừng): ");
                        int eqId = Integer.parseInt(scanner.nextLine());
                        if (eqId == 0) break;
                        System.out.print("Nhập số lượng: ");
                        int qty = Integer.parseInt(scanner.nextLine());
                        if (qty > 0) {
                            BookingDetail bd = new BookingDetail();
                            bd.setType("EQUIPMENT");
                            bd.setItemId(eqId);
                            bd.setQuantity(qty);
                            details.add(bd);
                        } else {
                            System.out.println("Số lượng phải lớn hơn 0!");
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Vui lòng nhập số hợp lệ.");
                    }
                }
            }

            // Save booking
            boolean success = bookingService.bookRoom(currentUser.getId(), roomId, startTime, endTime, details);
            if (success) {
                System.out.println("Đặt phòng thành công! Yêu cầu đang ở trạng thái PENDING.");
            } else {
                System.out.println("Đặt phòng thất bại. Vui lòng thử lại!");
            }

        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi: " + e.getMessage());
        }
    }
}
