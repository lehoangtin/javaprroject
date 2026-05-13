package com.parking.bll;

import com.parking.dao.*;
import com.parking.entity.Floor;
import com.parking.entity.ParkingInfo;
import com.parking.entity.Slot;
import com.parking.enums.SlotStatus;
import com.parking.enums.VehicleType;

import java.util.List;

public class SlotBLL {
    private SlotDAO slotDao = new SlotDAO();
    private ParkingInfoDAO infoDao = new ParkingInfoDAO();
    private FloorDAO floorDao = new FloorDAO(); 

    public List<Slot> getAllSlots() {
        return slotDao.getAllSlots();
    }

    public String addSlot(Long floorId, String slotNumber, VehicleType type, SlotStatus status) {
        if (slotNumber == null || slotNumber.trim().isEmpty()) {
            return "Số hiệu chỗ đỗ không được để trống!";
        }
        
        String cleanSlotNumber = slotNumber.trim();
        List<Slot> allSlots = slotDao.getAllSlots();
        int slotsInCurrentFloor = 0;
        for (Slot s : allSlots) {
            if (s.getFloorId().equals(floorId)) {
                slotsInCurrentFloor++;
                if (s.getSlotNumber().equalsIgnoreCase(cleanSlotNumber)) {
                    return "Lỗi: Số hiệu '" + cleanSlotNumber + "' đã tồn tại ở Tầng này. Vui lòng chọn số khác!";
                }
            }
        }
        List<Floor> floors = floorDao.getAllFloors();
        for (Floor f : floors) {
            if (f.getId().equals(floorId)) {
                if (f.getCapacity() != null && slotsInCurrentFloor >= f.getCapacity()) {
                    return "Hệ thống từ chối: Tầng này đã đạt giới hạn sức chứa (" + f.getCapacity() + " chỗ)!";
                }
                break;
            }
        }  

        Slot newSlot = new Slot();
        newSlot.setFloorId(floorId);
        newSlot.setSlotNumber(cleanSlotNumber);
        newSlot.setVehicleType(type);
        newSlot.setStatus(status);

        boolean isSuccess = slotDao.addSlot(newSlot);
        return isSuccess ? "SUCCESS" : "Lỗi hệ thống khi lưu xuống Database!";
    }

    public String updateSlot(Long id, Long floorId, String slotNumber, VehicleType type, SlotStatus status) {
        if (slotNumber == null || slotNumber.trim().isEmpty()) {
            return "Số hiệu chỗ đỗ không được để trống!";
        }

        String cleanSlotNumber = slotNumber.trim();

        List<Slot> allSlots = slotDao.getAllSlots();
        for (Slot s : allSlots) {
            if (!s.getId().equals(id)) {
                if (s.getFloorId().equals(floorId) && s.getSlotNumber().equalsIgnoreCase(cleanSlotNumber)) {
                    return "Lỗi: Số hiệu '" + cleanSlotNumber + "' đã bị trùng với một ô khác ở Tầng này!";
                }
            }
        }
        Slot updateSlot = new Slot();
        updateSlot.setId(id);
        updateSlot.setFloorId(floorId);
        updateSlot.setSlotNumber(cleanSlotNumber);
        updateSlot.setVehicleType(type);
        updateSlot.setStatus(status);

        boolean isSuccess = slotDao.updateSlot(updateSlot);
        return isSuccess ? "SUCCESS" : "Lỗi hệ thống khi cập nhật Database!";
    }

    public boolean deleteSlot(Long id) {
        return slotDao.deleteSlot(id);
    }

    public List<Slot> getSlotsByFloor(Long floorId) {
        return slotDao.getSlotsByFloor(floorId); 
    }

    public boolean updateSlotStatus(Long slotId, SlotStatus newStatus) {
        return slotDao.updateStatus(slotId, newStatus);
    }
}