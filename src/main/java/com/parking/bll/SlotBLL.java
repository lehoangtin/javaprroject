package com.parking.bll;

import com.parking.dao.SlotDAO;
import com.parking.entity.Slot;
import com.parking.enums.SlotStatus; // Import Enum
import com.parking.enums.VehicleType; // Import Enum
import java.util.List;

public class SlotBLL {
    private SlotDAO slotDAO = new SlotDAO();

    // Lấy toàn bộ dữ liệu cung cấp cho SlotMapPanel
    public List<Slot> getAllSlots() {
        return slotDAO.getAll();
    }

    // Thêm ô đỗ mới
    public boolean addSlot(Slot slot) {
        // Kiểm tra slotCode thay vì slotName
        if (slot.getSlotCode() == null || slot.getSlotCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã ô đỗ không được để trống!");
        }
        if (slot.getFloorId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn tầng cho ô đỗ!");
        }
        if (slot.getVehicleType() == null) {
            throw new IllegalArgumentException("Vui lòng chọn loại xe!");
        }
        
        // Mặc định khi mới tạo, ô đỗ sẽ ở trạng thái trống
        if (slot.getStatus() == null) {
            slot.setStatus(SlotStatus.EMPTY);
        }
        
        return slotDAO.insert(slot);
    }

    // Chuyển đổi trạng thái bằng Enum
    public boolean changeSlotStatus(Long slotId, SlotStatus newStatus) {
        if (slotId == null) {
            throw new IllegalArgumentException("Mã ô đỗ không hợp lệ!");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ!");
        }
        return slotDAO.updateStatus(slotId, newStatus);
    }

    // Xóa ô đỗ
    public boolean deleteSlot(Long slotId) {
        if (slotId == null) {
            throw new IllegalArgumentException("Mã ô đỗ không hợp lệ!");
        }
        boolean isDeleted = slotDAO.delete(slotId);
        if (!isDeleted) {
            throw new RuntimeException("Không thể xóa ô đỗ! Vui lòng đảm bảo ô đỗ đang trống (EMPTY).");
        }
        return true;
    }
}