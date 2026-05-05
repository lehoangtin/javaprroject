package com.parking.dao;

import com.parking.entity.PriceConfig;
import com.parking.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceConfigDAO {

    // 1. ĐỌC: Lấy danh sách biểu giá
    public List<PriceConfig> getAll() {
        List<PriceConfig> list = new ArrayList<>();
        String sql = "SELECT * FROM PriceConfig";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PriceConfig config = new PriceConfig();
                config.setId(rs.getLong("id"));
                config.setLotId(rs.getLong("lot_id"));
                config.setVehicleType(rs.getString("vehicle_type"));
                config.setBaseFee(rs.getBigDecimal("base_fee"));
                config.setExtraFeePerHour(rs.getBigDecimal("extra_fee_per_hour"));
                config.setMonthlyPrice(rs.getBigDecimal("monthly_price"));
                list.add(config);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. THÊM MỚI
    public boolean insert(PriceConfig config) {
        String sql = "INSERT INTO PriceConfig (lot_id, vehicle_type, base_fee, extra_fee_per_hour, monthly_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, config.getLotId() == null ? 1L : config.getLotId()); // Mặc định gán cho bãi xe số 1
            ps.setString(2, config.getVehicleType());
            ps.setBigDecimal(3, config.getBaseFee());
            ps.setBigDecimal(4, config.getExtraFeePerHour());
            ps.setBigDecimal(5, config.getMonthlyPrice());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 3. SỬA (CẬP NHẬT)
    public boolean update(PriceConfig config) {
        String sql = "UPDATE PriceConfig SET base_fee = ?, extra_fee_per_hour = ?, monthly_price = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, config.getBaseFee());
            ps.setBigDecimal(2, config.getExtraFeePerHour());
            ps.setBigDecimal(3, config.getMonthlyPrice());
            ps.setLong(4, config.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 4. XÓA
    public boolean delete(Long id) {
        String sql = "DELETE FROM PriceConfig WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}