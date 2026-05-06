package com.parking.bll;

import com.parking.dao.ParkingInfoDAO;
import com.parking.dao.SlotDAO;
import com.parking.entity.ParkingInfo;
import com.parking.entity.Slot;
import com.parking.enums.SlotStatus;
import com.parking.enums.VehicleType;

import java.util.List;

public class SlotBLL {
    private SlotDAO slotDao = new SlotDAO();
    private ParkingInfoDAO infoDao = new ParkingInfoDAO(); // Tận dụng code cũ

    public List<Slot> getAllSlots() {
        return slotDao.getAllSlots();
    }

    public String addSlot(Long floorId, String slotNumber, VehicleType vehicleType, SlotStatus status) {
        if (slotNumber == null || slotNumber.trim().isEmpty()) {
            return "Số hiệu vị trí không được để trống!";
        }

        // RÀNG BUỘC SỨC CHỨA
        ParkingInfo info = infoDao.getParkingInfo();
        if (info != null && info.getCapacity() != null) {
            int currentTotal = slotDao.countTotalSlots();
            if (currentTotal >= info.getCapacity()) {
                return "Lỗi: Bãi xe đã đạt giới hạn sức chứa (" + info.getCapacity() + " chỗ). Không thể tạo thêm!";
            }
        }

        Slot slot = new Slot();
        slot.setFloorId(floorId);
        slot.setSlotNumber(slotNumber);
        slot.setVehicleType(vehicleType);
        slot.setStatus(status);

        boolean success = slotDao.addSlot(slot);
        return success ? "SUCCESS" : "Lỗi hệ thống khi thêm chỗ đỗ!";
    }

    public String updateSlot(Long id, Long floorId, String slotNumber, VehicleType vehicleType, SlotStatus status) {
        if (slotNumber == null || slotNumber.trim().isEmpty()) {
            return "Số hiệu vị trí không được để trống!";
        }

        Slot slot = new Slot();
        slot.setId(id);
        slot.setFloorId(floorId);
        slot.setSlotNumber(slotNumber);
        slot.setVehicleType(vehicleType);
        slot.setStatus(status);

        boolean success = slotDao.updateSlot(slot);
        return success ? "SUCCESS" : "Lỗi hệ thống khi cập nhật!";
    }

    public boolean deleteSlot(Long id) {
        return slotDao.deleteSlot(id);
    }
}