package com.parking.bll;

import com.parking.dao.StatisticDAO;
import java.math.BigDecimal;

public class StatisticBLL {
    private StatisticDAO dao;

    public StatisticBLL() {
        this.dao = new StatisticDAO();
    }

    public int getParkedVehicleCount() {
        return dao.getParkedVehicleCount();
    }

    public int getEmptySlotCount() {
        return dao.getEmptySlotCount();
    }

    public int getActiveSubscriptionCount() {
        return dao.getActiveSubscriptionCount();
    }

    public BigDecimal getTotalRevenue() {
        return dao.getTotalRevenue();
    }
}