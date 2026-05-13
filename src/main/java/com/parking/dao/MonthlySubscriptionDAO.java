package com.parking.dao;

import com.parking.utils.DBConnection;
import com.parking.entity.MonthlySubscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonthlySubscriptionDAO {
    public boolean hasActiveSubscription(Long vehicleId) {
        String sql = "SELECT COUNT(*) FROM monthly_subscription " +
                     "WHERE vehicle_id = ? AND status = 'ACTIVE' " +
                     "AND CURDATE() BETWEEN start_date AND end_date";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setLong(1, vehicleId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return false;
    }
    public List<MonthlySubscription> getAllSubscriptions() {
        List<MonthlySubscription> list = new ArrayList<>();
        String sql = "SELECT * FROM monthly_subscription";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MonthlySubscription sub = new MonthlySubscription();
                
                sub.setId(rs.getLong("id")); 
                sub.setVehicleId(rs.getLong("vehicle_id"));
                
                if (rs.getDate("start_date") != null) {
                    sub.setStartDate(rs.getDate("start_date").toLocalDate());
                }
                if (rs.getDate("end_date") != null) {
                    sub.setEndDate(rs.getDate("end_date").toLocalDate());
                }
                
                sub.setAmountPaid(rs.getBigDecimal("amount_paid"));
                sub.setStatus(com.parking.enums.SubscriptionStatus.valueOf(rs.getString("status")));
                
                list.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addSubscription(MonthlySubscription sub) {
        String sql = "INSERT INTO monthly_subscription (vehicle_id, start_date, end_date, amount_paid, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (sub.getVehicleId() != null) {
                ps.setLong(1, sub.getVehicleId());
            } else {
                ps.setNull(1, Types.BIGINT);
            }
            
            ps.setDate(2, java.sql.Date.valueOf(sub.getStartDate()));
            ps.setDate(3, java.sql.Date.valueOf(sub.getEndDate()));
            ps.setBigDecimal(4, sub.getAmountPaid());
            ps.setString(5, sub.getStatus().name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSubscription(MonthlySubscription sub) {
        String sql = "UPDATE monthly_subscription SET vehicle_id = ?, start_date = ?, end_date = ?, amount_paid = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (sub.getVehicleId() != null) {
                ps.setLong(1, sub.getVehicleId());
            } else {
                ps.setNull(1, Types.BIGINT);
            }
            
            ps.setDate(2, java.sql.Date.valueOf(sub.getStartDate()));
            ps.setDate(3, java.sql.Date.valueOf(sub.getEndDate()));
            ps.setBigDecimal(4, sub.getAmountPaid());
            ps.setString(5, sub.getStatus().name());
            
            ps.setLong(6, sub.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSubscription(Long id) { 
        String sql = "DELETE FROM monthly_subscription WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}