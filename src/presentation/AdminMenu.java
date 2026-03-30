package presentation;

import model.Room;
import service.AdminService;
import service.EquipmentService;
import service.RoomService;
import model.Equipment;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private RoomService roomService = new RoomService();
    private EquipmentService equipmentService = new EquipmentService();
    private AdminService adminService = new AdminService();
    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== MENU QUẢN TRỊ VIÊN ===");
            System.out.println("1. Quản lý phòng họp");
            System.out.println("2. Quản lý thiết bị di động");
            System.out.println("3. Quản lý người dùng (Tạo tài khoản Support Staff)");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    manageRooms();
                    break;
                case "2":
                    manageEquipments();
                    break;
                case "3":
                    manageUsers();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void manageRooms() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ PHÒNG HỌP ---");
            System.out.println("1. Xem danh sách phòng");
            System.out.println("2. Thêm phòng mới");
            System.out.println("3. Cập nhật phòng");
            System.out.println("4. Xóa phòng");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Room> rooms = roomService.getAllRooms();
                    System.out.println("Danh sách phòng:");
                    for (Room r : rooms) {
                        System.out.printf("ID: %d | Tên: %s | Sức chứa: %d | Vị trí: %s | TB cố định: %s\n",
                                r.getId(), r.getName(), r.getCapacity(), r.getLocation(), r.getEquipmentDesc());
                    }
                    break;
                case "2":
                    try {
                        System.out.print("Tên phòng: "); String name = scanner.nextLine();
                        System.out.print("Sức chứa: "); int cap = Integer.parseInt(scanner.nextLine());
                        System.out.print("Vị trí: "); String loc = scanner.nextLine();
                        System.out.print("Thiết bị cố định: "); String eq = scanner.nextLine();
                        if (roomService.createRoom(name, cap, loc, eq)) {
                            System.out.println("Thêm phòng thành công!");
                        } else {
                            System.out.println("Thêm phòng thất bại!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Sức chứa phải là một số nguyên hợp lệ.");
                    }
                    break;
                case "3":
                    try {
                        System.out.print("ID phòng cần cập nhật: "); int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("Tên phòng mới: "); String nName = scanner.nextLine();
                        System.out.print("Sức chứa mới: "); int nCap = Integer.parseInt(scanner.nextLine());
                        System.out.print("Vị trí mới: "); String nLoc = scanner.nextLine();
                        System.out.print("Thiết bị cố định mới: "); String nEq = scanner.nextLine();
                        if (roomService.updateRoom(id, nName, nCap, nLoc, nEq)) {
                            System.out.println("Cập nhật thành công!");
                        } else {
                            System.out.println("Cập nhật thất bại!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi nhập liệu, ID và Sức chứa phải là số nguyên.");
                    }
                    break;
                case "4":
                    try {
                        System.out.print("ID phòng cần xóa: "); int dId = Integer.parseInt(scanner.nextLine());
                        if (roomService.deleteRoom(dId)) {
                            System.out.println("Xóa thành công!");
                        } else {
                            System.out.println("Xóa thất bại!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi nhập liệu, ID phải là số nguyên.");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void manageEquipments() {
        while (true) {
            System.out.println("\n--- QUẢN LÝ THIẾT BỊ ---");
            System.out.println("1. Xem danh sách thiết bị");
            System.out.println("2. Thêm thiết bị mới");
            System.out.println("3. Cập nhật số lượng thiết bị (cộng thêm hoặc giảm đi)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Equipment> eqs = equipmentService.getAllEquipment();
                    System.out.println("Danh sách thiết bị:");
                    for (Equipment e : eqs) {
                        System.out.printf("ID: %d | Tên: %s | Tổng số: %d | Khả dụng: %d | Trạng thái: %s\n",
                                e.getId(), e.getName(), e.getTotalQuantity(), e.getAvailableQuantity(), e.getStatus());
                    }
                    break;
                case "2":
                    try {
                        System.out.print("Tên thiết bị: "); String name = scanner.nextLine();
                        System.out.print("Tổng số lượng ban đầu: "); int qty = Integer.parseInt(scanner.nextLine());
                        if (equipmentService.addEquipment(name, qty)) {
                            System.out.println("Thêm thiết bị thành công!");
                        } else {
                            System.out.println("Thêm thiết bị thất bại!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Số lượng phải là một số nguyên hợp lệ.");
                    }
                    break;
                case "3":
                    try {
                        System.out.print("ID thiết bị: "); int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("Số lượng nhập thêm (nhập số âm nếu thanh lý/hỏng): "); int addQty = Integer.parseInt(scanner.nextLine());
                        if (equipmentService.updateEquipmentQuantity(id, addQty)) {
                            System.out.println("Cập nhật số lượng thành công!");
                        } else {
                            System.out.println("Cập nhật số lượng thất bại!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID và Số lượng phải là số nguyên.");
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void manageUsers() {
        System.out.println("\n--- TẠO TÀI KHOẢN NHÂN VIÊN HỖ TRỢ (SUPPORT STAFF) ---");
        System.out.print("Username: "); String user = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        System.out.print("Họ tên: "); String fn = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("SĐT: "); String phone = scanner.nextLine();

        if (adminService.createSupportStaff(user, pass, fn, email, phone)) {
            System.out.println("Tạo tài khoản Support Staff thành công!");
        } else {
            System.out.println("Tạo tài khoản thất bại!");
        }
    }
}
