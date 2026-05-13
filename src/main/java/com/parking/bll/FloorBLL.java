package com.parking.bll;

import com.parking.dao.FloorDAO;
import com.parking.dao.SlotDAO; // Cần dùng SlotDAO để đếm
import com.parking.entity.Floor;
import com.parking.entity.Slot;
import java.util.List;
public class FloorBLL {
    private FloorDAO floorDAO;
    private SlotDAO slotDAO;
    public FloorBLL() {
        this.floorDAO = new FloorDAO();
    }

    public List<Floor> getAllFloors() {
        return floorDAO.getAllFloors();
    }

    public boolean addFloor(Integer floorNumber, String description, Integer capacity) {
        if (floorNumber == null) return false;
        
        Floor floor = new Floor();
        floor.setFloorNumber(floorNumber);
        floor.setDescription(description);
        floor.setCapacity(capacity);
        
        return floorDAO.addFloor(floor);
    }

    public boolean updateFloor(Long id, Integer floorNumber, String description, Integer capacity) {
        if (floorNumber == null) return false;

        Floor floor = new Floor();
        floor.setId(id);
        floor.setFloorNumber(floorNumber);
        floor.setDescription(description);
        floor.setCapacity(capacity); 
        
        return floorDAO.updateFloor(floor);
    }

    public boolean deleteFloor(Long floorId) {
        List<Slot> allSlots = slotDAO.getAllSlots();
        boolean hasSlots = false;
        
        for (Slot slot : allSlots) {
            if (slot.getFloorId().equals(floorId)) {
                hasSlots = true;
                break;
            }
        }

        if (hasSlots) {
            System.err.println("Lỗi nghiệp vụ: Không thể xóa Tầng đang chứa Chỗ đỗ (Slot). Yêu cầu xóa các Slot trước!");
            return false; 
        }
        return floorDAO.deleteFloor(floorId);
    }
}