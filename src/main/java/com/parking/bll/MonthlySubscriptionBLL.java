package com.parking.bll;

import com.parking.dao.MonthlySubscriptionDAO;
import com.parking.dao.VehicleDAO;
import com.parking.entity.MonthlySubscription;
import com.parking.entity.Vehicle;
import com.parking.enums.VehicleType;

import java.math.BigDecimal;
import java.util.List;

public class MonthlySubscriptionBLL {
    private MonthlySubscriptionDAO dao;
    private VehicleDAO vehicleDAO;

    public MonthlySubscriptionBLL() {
        this.dao = new MonthlySubscriptionDAO();
        this.vehicleDAO = new VehicleDAO();
    }
    public List<MonthlySubscription> getAllSubscriptions() {
        return dao.getAllSubscriptions();
    }
    public boolean hasActiveSubscription(Long vehicleId) {
        if (vehicleId == null || vehicleId <= 0) {
            return false;
        }
        return dao.hasActiveSubscription(vehicleId);
    }
    public String saveSubscriptionFull(Long subId, String licensePlate, VehicleType type, 
                                       String ownerName, String ownerPhone, 
                                       MonthlySubscription sub) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return "Biển số xe không được để trống!";
        }
        if (sub.getStartDate() == null || sub.getEndDate() == null) {
            return "Ngày đăng ký và ngày hết hạn không hợp lệ!";
        }
        if (sub.getStartDate().isAfter(sub.getEndDate())) {
            return "Ngày đăng ký không thể diễn ra sau ngày hết hạn!";
        }
        if (sub.getAmountPaid() != null && sub.getAmountPaid().compareTo(BigDecimal.ZERO) < 0) {
            return "Số tiền thanh toán không được là số âm!";
        }

        String cleanPlate = licensePlate.trim().toUpperCase();

        Vehicle v = vehicleDAO.findByLicensePlate(cleanPlate);
        Long vehicleId;

        if (v == null) {
            v = new Vehicle();
            v.setLicensePlate(cleanPlate);
            v.setVehicleType(type);
            v.setOwnerName(ownerName);
            v.setOwnerPhone(ownerPhone);
            
            vehicleId = vehicleDAO.addVehicle(v);
            if (vehicleId == null) {
                return "Lỗi hệ thống khi khởi tạo thông tin xe mới!";
            }
        } else {
            vehicleId = v.getId();
            vehicleDAO.updateOwnerInfo(vehicleId, ownerName, ownerPhone);
        }

        sub.setVehicleId(vehicleId);
        sub.setId(subId); 

        boolean success;
        if (subId == null) {
            success = dao.addSubscription(sub);
        } else {
            success = dao.updateSubscription(sub);
        }

        return success ? "SUCCESS" : "Lỗi khi lưu dữ liệu vé tháng xuống Database!";
    }

    public boolean deleteSubscription(Long id) {
        if (id == null || id <= 0) {
            System.err.println("Lỗi: ID vé tháng không hợp lệ!");
            return false;
        }
        return dao.deleteSubscription(id);
    }
}