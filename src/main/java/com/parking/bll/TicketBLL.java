package com.parking.bll;

import java.util.List;

import com.parking.dao.MonthlySubscriptionDAO;
import com.parking.entity.MonthlySubscription;

public class TicketBLL {

    private MonthlySubscriptionDAO subscriptionDAO;

    public TicketBLL() {

        subscriptionDAO =
                new MonthlySubscriptionDAO();
    }

    // CHECK VÉ THÁNG CÒN HẠN
    public boolean isMonthlyTicketValid(
            Long vehicleId) {

        return subscriptionDAO
                .hasActiveSubscription(vehicleId);
    }

    // LẤY DANH SÁCH VÉ THÁNG
    public List<MonthlySubscription>
    getAllSubscriptions() {

        return subscriptionDAO
                .getAllSubscriptions();
    }

    // THÊM VÉ THÁNG
    public boolean addSubscription(
            MonthlySubscription subscription) {

        return subscriptionDAO
                .addSubscription(subscription);
    }

    // SỬA VÉ THÁNG
    public boolean updateSubscription(
            MonthlySubscription subscription) {

        return subscriptionDAO
                .updateSubscription(subscription);
    }

    // XÓA VÉ THÁNG
    public boolean deleteSubscription(
            String subscriptionId) {

        return subscriptionDAO
                .deleteSubscription(subscriptionId);
    }
}