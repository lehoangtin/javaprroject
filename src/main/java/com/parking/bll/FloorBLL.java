package com.parking.bll;

import com.parking.dao.FloorDAO;
import com.parking.dao.SlotDAO; // Cần dùng SlotDAO để đếm
import com.parking.entity.Floor;
import com.parking.entity.Slot;
import java.util.List;
public class FloorBLL {
    private FloorDAO floorDAO;
    private SlotDAO slotDAO; // Thêm dòng này
    public FloorBLL() {
        this.floorDAO = new FloorDAO();
    }

    public List<Floor> getAllFloors() {
        return floorDAO.getAllFloors();
    }

    public boolean addFloor(Integer floorNumber, String description) {
        if (floorNumber == null) return false;
        
        Floor floor = new Floor();
        floor.setFloorNumber(floorNumber);
        floor.setDescription(description);
        
        return floorDAO.addFloor(floor);
    }

    public boolean updateFloor(Long id, Integer floorNumber, String description) {
        if (floorNumber == null) return false;

        Floor floor = new Floor();
        floor.setId(id);
        floor.setFloorNumber(floorNumber);
        floor.setDescription(description);
        
        return floorDAO.updateFloor(floor);
    }

    public boolean deleteFloor(Long floorId) {
        // KIỂM TRA RÀNG BUỘC KHÓA NGOẠI: Tầng này có Slot nào không?
        List<Slot> allSlots = slotDAO.getAllSlots();
        boolean hasSlots = false;
        
        for (Slot slot : allSlots) {
            if (slot.getFloorId().equals(floorId)) {
                hasSlots = true;
                break; // Tìm thấy 1 cái là đủ để chặn rồi
            }
        }

        if (hasSlots) {
            System.err.println("Lỗi nghiệp vụ: Không thể xóa Tầng đang chứa Chỗ đỗ (Slot). Yêu cầu xóa các Slot trước!");
            return false; // Chặn không cho gọi DAO
        }

        // Vượt qua kiểm tra an toàn thì mới cho phép xóa
        return floorDAO.deleteFloor(floorId);
    }
}