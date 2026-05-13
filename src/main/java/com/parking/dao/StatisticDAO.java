package com.parking.dao;

import com.parking.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class StatisticDAO {

    public int getParkedVehicleCount() {
        String sql = "SELECT COUNT(*) FROM parking_record WHERE status = 'IN_PARKING'";
        return executeCountQuery(sql);
    }

    public int getEmptySlotCount() {
        String sql = "SELECT COUNT(*) FROM slot WHERE status = 'EMPTY'";
        return executeCountQuery(sql);
    }

    public int getActiveSubscriptionCount() {
        String sql = "SELECT COUNT(*) FROM monthly_subscription WHERE status = 'ACTIVE' AND CURDATE() BETWEEN start_date AND end_date";
        return executeCountQuery(sql);
    }

    public BigDecimal getTotalRevenue() {
        BigDecimal total = BigDecimal.ZERO;
        
        String sqlRecord = "SELECT SUM(fee) FROM parking_record WHERE status = 'COMPLETED'";
        String sqlSub = "SELECT SUM(amount_paid) FROM monthly_subscription";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlRecord);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getBigDecimal(1) != null) {
                    total = total.add(rs.getBigDecimal(1));
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlSub);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getBigDecimal(1) != null) {
                    total = total.add(rs.getBigDecimal(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    private int executeCountQuery(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}