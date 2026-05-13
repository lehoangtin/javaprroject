package com.parking.dao;

import com.parking.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

public class StatisticDAO {

    // 1. Đếm số xe đang trong bãi
    public int getParkedVehicleCount() {
        String sql = "SELECT COUNT(*) FROM parking_record WHERE status = 'IN_PARKING'";
        return executeCountQuery(sql);
    }

    // 2. Đếm số chỗ đỗ còn trống
    public int getEmptySlotCount() {
        String sql = "SELECT COUNT(*) FROM slot WHERE status = 'EMPTY'";
        return executeCountQuery(sql);
    }

    // 3. Đếm số vé tháng đang hoạt động
    public int getActiveSubscriptionCount() {
        String sql = "SELECT COUNT(*) FROM monthly_subscription WHERE status = 'ACTIVE' AND CURDATE() BETWEEN start_date AND end_date";
        return executeCountQuery(sql);
    }

    // 4. Tính tổng doanh thu (Vé lượt + Vé tháng)
    public BigDecimal getTotalRevenue() {
        BigDecimal total = BigDecimal.ZERO;
        
        // Doanh thu vé lượt (Đã thanh toán)
        String sqlRecord = "SELECT SUM(fee) FROM parking_record WHERE status = 'COMPLETED'";
        // Doanh thu vé tháng
        String sqlSub = "SELECT SUM(amount_paid) FROM monthly_subscription";

        try (Connection conn = DBConnection.getConnection()) {
            // Lấy doanh thu vé lượt
            try (PreparedStatement ps = conn.prepareStatement(sqlRecord);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getBigDecimal(1) != null) {
                    total = total.add(rs.getBigDecimal(1));
                }
            }
            // Lấy doanh thu vé tháng
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

    // Hàm tiện ích chạy các câu lệnh COUNT
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