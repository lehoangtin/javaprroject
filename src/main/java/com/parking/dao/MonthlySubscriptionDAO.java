package com.parking.dao;

import com.parking.entity.MonthlySubscription;
import com.parking.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonthlySubscriptionDAO {

    // [READ] Lấy danh sách tất cả các lượt đăng ký vé tháng
    public List<MonthlySubscription> getAllSubscriptions() {
        List<MonthlySubscription> list = new ArrayList<>();
        String sql = "SELECT * FROM monthly_subscription ORDER BY id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                MonthlySubscription sub = new MonthlySubscription();
                sub.setId(rs.getLong("id"));
                sub.setVehicleId(rs.getLong("vehicle_id"));
                
                // Chuyển đổi java.sql.Date sang java.time.LocalDate
                sub.setStartDate(rs.getDate("start_date").toLocalDate());
                sub.setEndDate(rs.getDate("end_date").toLocalDate());
                
                sub.setAmountPaid(rs.getBigDecimal("amount_paid"));
                sub.setStatus(rs.getString("status"));
                list.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // [CREATE] Thêm một lượt đăng ký/gia hạn mới
    public boolean addSubscription(MonthlySubscription sub) {
        String sql = "INSERT INTO monthly_subscription (vehicle_id, start_date, end_date, amount_paid, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setLong(1, sub.getVehicleId());
            ps.setDate(2, Date.valueOf(sub.getStartDate()));
            ps.setDate(3, Date.valueOf(sub.getEndDate()));
            ps.setBigDecimal(4, sub.getAmountPaid());
            ps.setString(5, sub.getStatus());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [UPDATE] Cập nhật thông tin hợp đồng (VD: Hủy vé, sửa sai sót)
    public boolean updateSubscription(MonthlySubscription sub) {
        String sql = "UPDATE monthly_subscription SET vehicle_id = ?, start_date = ?, end_date = ?, amount_paid = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setLong(1, sub.getVehicleId());
            ps.setDate(2, Date.valueOf(sub.getStartDate()));
            ps.setDate(3, Date.valueOf(sub.getEndDate()));
            ps.setBigDecimal(4, sub.getAmountPaid());
            ps.setString(5, sub.getStatus());
            ps.setLong(6, sub.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}