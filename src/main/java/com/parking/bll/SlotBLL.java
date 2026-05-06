package com.parking.bll;

import com.parking.dao.*;
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

    public String addSlot(Long floorId, String slotNumber, VehicleType type, SlotStatus status) {
        if (slotNumber == null || slotNumber.trim().isEmpty()) {
            return "Số hiệu chỗ đỗ không được để trống!";
        }
        
        String cleanSlotNumber = slotNumber.trim();

        // --- 1. KIỂM TRA TRÙNG LẶP SỐ HIỆU TRÊN CÙNG MỘT TẦNG ---
        List<Slot> allSlots = slotDao.getAllSlots();
        for (Slot s : allSlots) {
            // Nếu cùng ID Tầng VÀ trùng Số hiệu (không phân biệt chữ hoa/thường)
            if (s.getFloorId().equals(floorId) && s.getSlotNumber().equalsIgnoreCase(cleanSlotNumber)) {
                return "Lỗi: Số hiệu '" + cleanSlotNumber + "' đã tồn tại ở Tầng này. Vui lòng chọn số khác!";
            }
        }

        // --- 2. KIỂM TRA SỨC CHỨA TỐI ĐA (Đoạn code tôi gửi bạn hôm trước) ---
        ParkingInfo info = infoDao.getParkingInfo();
        if (info != null) {
            if (allSlots.size() >= info.getCapacity()) {
                return "Hệ thống từ chối: Bãi xe đã đạt sức chứa tối đa (" + info.getCapacity() + " chỗ)!";
            }
        }

        // --- 3. TIẾN HÀNH LƯU XUỐNG DB ---
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

        // --- KIỂM TRA TRÙNG LẶP KHI SỬA ---
        List<Slot> allSlots = slotDao.getAllSlots();
        for (Slot s : allSlots) {
            // Bỏ qua chính cái Slot đang được sửa (so sánh ID)
            if (!s.getId().equals(id)) {
                // Kiểm tra xem có trùng với Slot NÀO KHÁC trên cùng Tầng không
                if (s.getFloorId().equals(floorId) && s.getSlotNumber().equalsIgnoreCase(cleanSlotNumber)) {
                    return "Lỗi: Số hiệu '" + cleanSlotNumber + "' đã bị trùng với một ô khác ở Tầng này!";
                }
            }
        }

        // --- TIẾN HÀNH CẬP NHẬT DB ---
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
        // Gọi xuống DAO để lấy danh sách Slot có floor_id tương ứng
        return slotDao.getSlotsByFloor(floorId); 
    }
 // Viết sẵn hàm này trong SlotBLL cho Phi dùng
    public boolean updateSlotStatus(Long slotId, SlotStatus newStatus) {
        // Gọi xuống DAO để update riêng cột status của slot này
        return slotDao.updateStatus(slotId, newStatus);
    }
}