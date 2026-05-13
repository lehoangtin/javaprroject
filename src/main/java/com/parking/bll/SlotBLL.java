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
    private ParkingInfoDAO infoDao = new ParkingInfoDAO(); // Tận dụng code cũ
    private FloorDAO floorDao = new FloorDAO(); // THÊM MỚI: Để lấy thông tin sức chứa của Tầng

    public List<Slot> getAllSlots() {
        return slotDao.getAllSlots();
    }

    public String addSlot(Long floorId, String slotNumber, VehicleType type, SlotStatus status) {
        if (slotNumber == null || slotNumber.trim().isEmpty()) {
            return "Số hiệu chỗ đỗ không được để trống!";
        }
        
        String cleanSlotNumber = slotNumber.trim();
        List<Slot> allSlots = slotDao.getAllSlots();
        int slotsInCurrentFloor = 0; // Biến đếm số lượng slot hiện tại của tầng này

        // --- 1. KIỂM TRA TRÙNG LẶP SỐ HIỆU TRÊN CÙNG MỘT TẦNG VÀ ĐẾM SỐ SLOT ---
        for (Slot s : allSlots) {
            if (s.getFloorId().equals(floorId)) {
                slotsInCurrentFloor++; // Tăng biến đếm nếu slot này thuộc về tầng đang muốn thêm

                // Nếu trùng Số hiệu (không phân biệt chữ hoa/thường)
                if (s.getSlotNumber().equalsIgnoreCase(cleanSlotNumber)) {
                    return "Lỗi: Số hiệu '" + cleanSlotNumber + "' đã tồn tại ở Tầng này. Vui lòng chọn số khác!";
                }
            }
        }

        // --- 2. KIỂM TRA SỨC CHỨA TỐI ĐA CỦA RIÊNG TẦNG ĐÓ ---
        List<Floor> floors = floorDao.getAllFloors();
        for (Floor f : floors) {
            if (f.getId().equals(floorId)) {
                // Nếu tầng có cấu hình capacity và số slot hiện tại đã đạt hoặc vượt ngưỡng
                if (f.getCapacity() != null && slotsInCurrentFloor >= f.getCapacity()) {
                    return "Hệ thống từ chối: Tầng này đã đạt giới hạn sức chứa (" + f.getCapacity() + " chỗ)!";
                }
                break;
            }
        }  

        // --- 4. TIẾN HÀNH LƯU XUỐNG DB ---
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