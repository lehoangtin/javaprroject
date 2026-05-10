package com.parking.bll;

import com.parking.dao.ParkingRecordDAO;
import com.parking.dao.VehicleDAO;
import com.parking.entity.ParkingRecord;
import com.parking.entity.Vehicle;
import com.parking.enums.RecordStatus;
import com.parking.enums.SlotStatus;
import com.parking.enums.VehicleType;
import com.parking.utils.Session;

import java.time.LocalDateTime;

public class ParkingRecordBLL {
    private ParkingRecordDAO recordDAO = new ParkingRecordDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    
    // Gọi sang BLL của Tín để tái sử dụng code
    private SlotBLL slotBLL = new SlotBLL(); 
    
    private com.parking.dao.MonthlySubscriptionDAO subDAO = new com.parking.dao.MonthlySubscriptionDAO();
    private com.parking.bll.PriceConfigBLL priceBLL = new com.parking.bll.PriceConfigBLL(); // Của Tín

 
    // HÀM 1: CHỈ TÍNH TOÁN (Dùng cho nút Tra cứu - Không ghi đè DB)
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
        return record; // Trả về để hiển thị, chưa lưu vào DB
    }

    // HÀM 2: XÁC NHẬN RA XE (Dùng cho nút Thanh toán - Lúc này mới ghi DB)
    public boolean confirmCheckOut(ParkingRecord record) {
        record.setStatus(RecordStatus.COMPLETED);
        if (recordDAO.updateRecordOnCheckout(record)) {
            return slotBLL.updateSlotStatus(record.getSlotId(), SlotStatus.EMPTY);
        }
        return false;
    }
    public String checkIn(String licensePlate, VehicleType type, Long slotId) {
        // 1. Validate: Kiểm tra phiên đăng nhập xem ai đang trực ca
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
        
        // Kiểm tra xem xe này có đang đỗ trong bãi hay không
        ParkingRecord activeRecord = recordDAO.findActiveRecordByLicensePlate(cleanPlate);
        if (activeRecord != null) {
            return "Xe mang biển số '" + cleanPlate + "' hiện đang trong bãi";
        }

        // 2. Xử lý thông tin Xe (Vehicle)
        Vehicle vehicle = vehicleDAO.findByLicensePlate(cleanPlate);
        Long currentVehicleId;
        
        if (vehicle != null) {
            currentVehicleId = vehicle.getId();
        } else {
            // Xe mới lần đầu vào bãi -> Thêm vào DB
            Vehicle newVehicle = new Vehicle();
            newVehicle.setLicensePlate(cleanPlate);
            newVehicle.setVehicleType(type);
            currentVehicleId = vehicleDAO.addVehicle(newVehicle);
            
            if (currentVehicleId == null) {
                return "Lỗi: Không thể khởi tạo thông tin xe vào hệ thống";
            }
        }

        // 3. Tạo Record Giao dịch (ParkingRecord)
        ParkingRecord record = new ParkingRecord();
        record.setTimeIn(LocalDateTime.now());
        record.setVehicleId(currentVehicleId);
        record.setSlotId(slotId);
        record.setStaffId(Session.currentUser.getId()); // Ghi nhận nhân viên thao tác
        record.setStatus(RecordStatus.IN_PARKING); // Trạng thái: Đang đỗ trong bãi

        // 4. Lưu giao dịch & Cập nhật Sơ đồ
        if (recordDAO.insertRecord(record)) {
            // Gọi hàm của Tín để ép ô đỗ chuyển sang màu đỏ (OCCUPIED)
            boolean isSlotUpdated = slotBLL.updateSlotStatus(slotId, SlotStatus.OCCUPIED);
            
            if (isSlotUpdated) {
                return "SUCCESS";
            } else {
                return "Nhận xe thành công nhưng hệ thống báo lỗi khi cập nhật sơ đồ. Vui lòng báo Admin";
            }
        }

        return "Lỗi hệ thống khi tạo giao dịch Check-in";
    }
}