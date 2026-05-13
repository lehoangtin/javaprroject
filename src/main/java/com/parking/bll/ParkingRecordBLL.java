package com.parking.bll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.parking.dao.ParkingRecordDAO;
import com.parking.dao.VehicleDAO;
import com.parking.entity.ParkingRecord;
import com.parking.entity.Vehicle;
import com.parking.enums.RecordStatus;
import com.parking.enums.SlotStatus;
import com.parking.enums.VehicleType;
import com.parking.utils.Session;

public class ParkingRecordBLL {
    private ParkingRecordDAO recordDAO = new ParkingRecordDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    
    private SlotBLL slotBLL = new SlotBLL(); 
    
    private com.parking.dao.MonthlySubscriptionDAO subDAO = new com.parking.dao.MonthlySubscriptionDAO();
    private com.parking.bll.PriceConfigBLL priceBLL = new com.parking.bll.PriceConfigBLL(); 
    public ParkingRecord calculateInfo(String licensePlate) throws Exception {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập biển số xe!");
        }
        String cleanPlate = licensePlate.trim().toUpperCase();
        ParkingRecord record = recordDAO.findActiveRecordByLicensePlate(cleanPlate);
        if (record == null) {
            throw new Exception("Không tìm thấy xe '" + cleanPlate + "' trong bãi!");
        }

        Vehicle vehicle = vehicleDAO.findById(record.getVehicleId());
        LocalDateTime timeOut = LocalDateTime.now();
        java.math.BigDecimal fee = java.math.BigDecimal.ZERO;

        boolean hasActiveSub = subDAO.hasActiveSubscription(vehicle.getId());
        if (!hasActiveSub) {
            com.parking.entity.PriceConfig config = priceBLL.getConfigByVehicleType(vehicle.getVehicleType());
            if (config != null) {
                long durationMinutes = java.time.Duration.between(record.getTimeIn(), timeOut).toMinutes();
                long hours = (long) Math.ceil(durationMinutes / 60.0);
                fee = config.getBaseFee();
                if (hours > 1) {
                    fee = fee.add(config.getExtraFeePerHour().multiply(new java.math.BigDecimal(hours - 1)));
                }
            }
        }
        record.setTimeOut(timeOut);
        record.setFee(fee);
        return record; 
    }

    public boolean confirmCheckOut(ParkingRecord record) {
        record.setStatus(RecordStatus.COMPLETED);
        if (recordDAO.updateRecordOnCheckout(record)) {
            return slotBLL.updateSlotStatus(record.getSlotId(), SlotStatus.EMPTY);
        }
        return false;
    }
    public String checkIn(String licensePlate, VehicleType type, Long slotId) {
        if (Session.currentUser == null) {
            return "Lỗi: Không xác định được nhân viên đang trực ca (Chưa đăng nhập)";
        }
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return "Lỗi: Biển số xe không được để trống!";
        }
        if (slotId == null) {
            return "Lỗi: Vui lòng chọn một vị trí đỗ (Slot) trên màn hình";
        }

        String cleanPlate = licensePlate.trim().toUpperCase();
        
        ParkingRecord activeRecord = recordDAO.findActiveRecordByLicensePlate(cleanPlate);
        if (activeRecord != null) {
            return "Xe mang biển số '" + cleanPlate + "' hiện đang trong bãi";
        }

        Vehicle vehicle = vehicleDAO.findByLicensePlate(cleanPlate);
        Long currentVehicleId;
        
        if (vehicle != null) {
            currentVehicleId = vehicle.getId();
        } else {
            Vehicle newVehicle = new Vehicle();
            newVehicle.setLicensePlate(cleanPlate);
            newVehicle.setVehicleType(type);
            currentVehicleId = vehicleDAO.addVehicle(newVehicle);
            
            if (currentVehicleId == null) {
                return "Lỗi: Không thể khởi tạo thông tin xe vào hệ thống";
            }
        }

        ParkingRecord record = new ParkingRecord();
        record.setTimeIn(LocalDateTime.now());
        record.setVehicleId(currentVehicleId);
        record.setSlotId(slotId);
        record.setStaffId(Session.currentUser.getId()); // Ghi nhận nhân viên thao tác
        record.setStatus(RecordStatus.IN_PARKING); // Trạng thái: Đang đỗ trong bãi

        if (recordDAO.insertRecord(record)) {
            boolean isSlotUpdated = slotBLL.updateSlotStatus(slotId, SlotStatus.OCCUPIED);
            
            if (isSlotUpdated) {
                return "SUCCESS";
            } else {
                return "Nhận xe thành công nhưng hệ thống báo lỗi khi cập nhật sơ đồ. Vui lòng báo Admin";
            }
        }

        return "Lỗi hệ thống khi tạo giao dịch Check-in";
    }
    public List<Map<String, Object>> getAllHistory() {
        return recordDAO.getTransactionHistory();
    }
    public Map<String, String> getVehicleInfoInSlot(String slotNumber) {
        return recordDAO.getVehicleInfoInSlot(slotNumber);
    }
}