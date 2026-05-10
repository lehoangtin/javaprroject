package com.parking.dao;
import com.parking.utils.DBConnection;
import java.sql.*;
//Để kiểm tra vé tháng của Thịnh
public class MonthlySubscriptionDAO {
    // Kiểm tra xe có vé tháng còn hạn hay không
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
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}