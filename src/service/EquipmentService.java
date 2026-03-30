package service;

import dao.EquipmentDAO;
import model.Equipment;
import java.util.List;

public class EquipmentService {
    private EquipmentDAO equipmentDAO = new EquipmentDAO();

    public boolean addEquipment(String name, int totalQuantity) {
        if (totalQuantity <= 0) {
            System.err.println("Số lượng phải lớn hơn 0.");
            return false;
        }
        Equipment eq = new Equipment();
        eq.setName(name);
        eq.setTotalQuantity(totalQuantity);
        eq.setAvailableQuantity(totalQuantity);
        eq.setStatus("ACTIVE");
        return equipmentDAO.addEquipment(eq);
    }

    public List<Equipment> getAllEquipment() {
        return equipmentDAO.getAllEquipment();
    }

    public boolean updateEquipmentQuantity(int id, int addedQuantity) {
        return equipmentDAO.updateAvailableQuantity(id, addedQuantity);
    }
}
