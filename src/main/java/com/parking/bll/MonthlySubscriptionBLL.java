package com.parking.bll;

import com.parking.dao.MonthlySubscriptionDAO;
import com.parking.entity.MonthlySubscription;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MonthlySubscriptionBLL {
    private MonthlySubscriptionDAO dao;

    public MonthlySubscriptionBLL() {
        this.dao = new MonthlySubscriptionDAO();
    }

    public List<MonthlySubscription> getAll() {
        return dao.getAllSubscriptions();
    }

    public boolean registerMonthly(Long vehicleId, LocalDate startDate, LocalDate endDate, BigDecimal amountPaid, String status) {
        // Kiểm tra logic: Ngày kết thúc không được trước ngày bắt đầu
        if (endDate.isBefore(startDate)) {
            return false;
        }
        // Kiểm tra tiền hợp lệ
        if (amountPaid == null || amountPaid.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        MonthlySubscription sub = new MonthlySubscription();
        sub.setVehicleId(vehicleId);
        sub.setStartDate(startDate);
        sub.setEndDate(endDate);
        sub.setAmountPaid(amountPaid);
        sub.setStatus(status);

        return dao.addSubscription(sub);
    }

    public boolean updateSubscription(Long id, Long vehicleId, LocalDate startDate, LocalDate endDate, BigDecimal amountPaid, String status) {
        if (endDate.isBefore(startDate) || amountPaid.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        MonthlySubscription sub = new MonthlySubscription();
        sub.setId(id);
        sub.setVehicleId(vehicleId);
        sub.setStartDate(startDate);
        sub.setEndDate(endDate);
        sub.setAmountPaid(amountPaid);
        sub.setStatus(status);

        return dao.updateSubscription(sub);
    }
}