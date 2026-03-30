package presentation;

import model.User;
import model.Booking;
import service.SupportStaffService;

import java.util.List;
import java.util.Scanner;

public class SupportStaffMenu {
    private User currentUser;
    private SupportStaffService staffService = new SupportStaffService();
    private Scanner scanner = new Scanner(System.in);

    public SupportStaffMenu(User user) {
        this.currentUser = user;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== MENU NHÂN VIÊN HỖ TRỢ ===");
            System.out.println("Xin chào, " + currentUser.getFullName());
            System.out.println("1. Xem danh sách công việc & Cập nhật trạng thái chuẩn bị");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manageTasks();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void manageTasks() {
        System.out.println("\n--- CÔNG VIỆC CỦA TÔI ---");
        List<Booking> tasks = staffService.getAssignedTasks(currentUser.getId());
        if (tasks.isEmpty()) {
            System.out.println("Bạn chưa có công việc nào được phân công.");
            return;
        }

        for (Booking b : tasks) {
            System.out.printf("ID: %d | Room ID: %d | Time: %s to %s | Prep: %s\n",
                    b.getId(), b.getRoomId(), b.getStartTime(), b.getEndTime(), b.getPrepStatus());
        }

        System.out.print("Nhập ID công việc muốn cập nhật trạng thái (hoặc 0 để hủy): ");
        int bookingId = Integer.parseInt(scanner.nextLine());
        if (bookingId == 0) return;

        System.out.println("Chọn trạng thái chuẩn bị:");
        System.out.println("1. Lấy thiết bị (Preparing)");
        System.out.println("2. Đã sẵn sàng (Ready)");
        System.out.println("3. Thiếu thiết bị (Missing equipment)");
        System.out.print("Mời đổi trạng thái (1/2/3): ");
        String prepChoice = scanner.nextLine();
        String status = "";
        if (prepChoice.equals("1")) status = "PREPARING";
        else if (prepChoice.equals("2")) status = "READY";
        else if (prepChoice.equals("3")) status = "MISSING_EQUIPMENT";
        else {
            System.out.println("Lựa chọn không hợp lệ.");
            return;
        }

        boolean success = staffService.updatePrepStatus(bookingId, status);
        System.out.println(success ? "Cập nhật thành công!" : "Lỗi cập nhật!");
    }
}
