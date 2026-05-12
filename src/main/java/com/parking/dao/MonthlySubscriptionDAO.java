package com.parking.dao;

import com.parking.utils.DBConnection;
import com.parking.entity.MonthlySubscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Để kiểm tra vé tháng của Thịnh
public class MonthlySubscriptionDAO {

    // KIỂM TRA XE CÓ VÉ THÁNG CÒN HẠN HAY KHÔNG
    public boolean hasActiveSubscription(Long vehicleId) {

        String sql =
                "SELECT COUNT(*) FROM monthly_subscription " +
                "WHERE vehicle_id = ? " +
                "AND status = 'ACTIVE' " +
                "AND CURDATE() BETWEEN start_date AND end_date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, vehicleId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    // LẤY TOÀN BỘ
    public List<MonthlySubscription> getAllSubscriptions() {

        List<MonthlySubscription> list =
                new ArrayList<>();

        String sql =
                "SELECT * FROM monthly_subscription";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                MonthlySubscription subscription =
                        new MonthlySubscription();

                subscription.setSubscriptionId(
                        rs.getString("subscription_id"));

                subscription.setStartDate(
                        rs.getDate("start_date"));

                subscription.setEndDate(
                        rs.getDate("end_date"));

                list.add(subscription);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return list;
    }

    // THÊM
    public boolean addSubscription(
            MonthlySubscription subscription) {

        String sql =
                "INSERT INTO monthly_subscription " +
                "(subscription_id, start_date, end_date) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(
                    1,
                    subscription.getSubscriptionId());

            ps.setDate(
                    2,
                    new java.sql.Date(
                            subscription.getStartDate().getTime()));

            ps.setDate(
                    3,
                    new java.sql.Date(
                            subscription.getEndDate().getTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    // SỬA
    public boolean updateSubscription(
            MonthlySubscription subscription) {

        String sql =
                "UPDATE monthly_subscription " +
                "SET start_date = ?, end_date = ? " +
                "WHERE subscription_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(
                    1,
                    new java.sql.Date(
                            subscription.getStartDate().getTime()));

            ps.setDate(
                    2,
                    new java.sql.Date(
                            subscription.getEndDate().getTime()));

            ps.setString(
                    3,
                    subscription.getSubscriptionId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }

    // XÓA
    public boolean deleteSubscription(
            String subscriptionId) {

        String sql =
                "DELETE FROM monthly_subscription " +
                "WHERE subscription_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, subscriptionId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;
    }
}