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
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleBooking();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Tính năng đang phát triển hoặc lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleBooking() {
        try {
            System.out.println("\n--- ĐẶT PHÒNG HỌP ---");
            System.out.print("Nhập thời gian bắt đầu (vd: yyyy-MM-dd HH:mm): ");
            String startStr = scanner.nextLine();
            System.out.print("Nhập thời gian kết thúc (vd: yyyy-MM-dd HH:mm): ");
            String endStr = scanner.nextLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startDT = LocalDateTime.parse(startStr, formatter);
            LocalDateTime endDT = LocalDateTime.parse(endStr, formatter);
            Timestamp startTime = Timestamp.valueOf(startDT);
            Timestamp endTime = Timestamp.valueOf(endDT);

            if (!startDT.isBefore(endDT)) {
                System.out.println("Lỗi: Thời gian kết thúc phải sau thời gian bắt đầu!");
                return;
            }
            if (startDT.isBefore(LocalDateTime.now())) {
                System.out.println("Lỗi: Không thể đặt phòng trong quá khứ!");
                return;
            }

            System.out.print("Nhập số người tham gia dự kiến: ");
            int initCapacity = Integer.parseInt(scanner.nextLine());

            List<Room> availableRooms = roomService.getAvailableRooms(startTime, endTime, initCapacity);
            if (availableRooms.isEmpty()) {
                System.out.println("Không có phòng trống nào phù hợp với yêu cầu của bạn.");
                return;
            }

            System.out.println("\nDanh sách phòng trống:");
            for (Room r : availableRooms) {
                System.out.printf("ID: %d | Tên: %s | Sức chứa: %d | Vị trí: %s%n",
                                  r.getId(), r.getName(), r.getCapacity(), r.getLocation());
            }
            System.out.print("Nhập ID phòng muốn đặt: ");
            int roomId = Integer.parseInt(scanner.nextLine());

            // Check valid id
            boolean valid = availableRooms.stream().anyMatch(r -> r.getId() == roomId);
            if (!valid) {
                 System.out.println("ID phòng không hợp lệ.");
                 return;
            }

            // Allow selecting extra equipment
            List<BookingDetail> details = new ArrayList<>();
            System.out.print("Bạn có muốn mượn thêm thiết bị không? (Y/N): ");
            String yesNo = scanner.nextLine();
            if (yesNo.equalsIgnoreCase("Y")) {
                List<Equipment> configs = equipmentService.getAllEquipment();
                System.out.println("Danh sách thiết bị có thể mượn:");
                for (Equipment eq : configs) {
                   System.out.printf("ID: %d | Tên: %s | Khả dụng: %d%n", eq.getId(), eq.getName(), eq.getAvailableQuantity());
                }
                while (true) {
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
            System.out.println("Lỗi nhập liệu: " + e.getMessage());
        }
    }
}
