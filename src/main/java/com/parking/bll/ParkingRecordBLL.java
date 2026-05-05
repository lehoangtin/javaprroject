package com.parking.bll;

import com.parking.dao.ParkingRecordDAO;
import com.parking.dao.SlotDAO;
import com.parking.entity.ParkingRecord;
import com.parking.enums.RecordStatus; //
import com.parking.enums.SlotStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ParkingRecordBLL {
    private ParkingRecordDAO recordDAO = new ParkingRecordDAO();
    private SlotDAO slotDAO = new SlotDAO();

    // Lấy xe đang đỗ
    public List<ParkingRecord> getActiveRecords() {
        return recordDAO.getActiveRecords();
    }

    // --- CHECK-IN (Nhận xe) ---
    public boolean processCheckIn(Long vehicleId, Long slotId, Long ticketId) {
        if (vehicleId == null || slotId == null) {
            throw new IllegalArgumentException("Dữ liệu xe hoặc ô đỗ không hợp lệ!");
        }

        ParkingRecord record = new ParkingRecord();
        record.setVehicleId(vehicleId);
        record.setSlotId(slotId);
        record.setTicketId(ticketId);
        record.setTimeIn(LocalDateTime.now());
        record.setStatus(RecordStatus.IN_PARKING); // Gán trạng thái đang đỗ

        boolean isInserted = recordDAO.insert(record);
        
        // Nhận xe thành công thì đổi màu ô đỗ thành Đỏ (TAKEN)
        if (isInserted) {
            slotDAO.updateStatus(slotId, SlotStatus.OCCUPIED);
        }
        return isInserted;
    }

    // --- CHECK-OUT (Trả xe) ---
    public boolean processCheckOut(ParkingRecord record, BigDecimal calculatedFee) {
        if (record == null || record.getId() == null) {
            throw new IllegalArgumentException("Không tìm thấy thông tin vé!");
        }

        record.setTimeOut(LocalDateTime.now());
        record.setFee(calculatedFee);
        record.setStatus(RecordStatus.COMPLETED); // Gán trạng thái đã hoàn thành

        boolean isUpdated = recordDAO.updateCheckOut(record);
        
        // Trả xe thành công thì trả ô đỗ về màu Xanh (EMPTY)
        if (isUpdated) {
            slotDAO.updateStatus(record.getSlotId(), SlotStatus.EMPTY);
        }
        return isUpdated;
    }
}