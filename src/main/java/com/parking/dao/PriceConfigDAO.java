package com.parking.dao;

import com.parking.entity.PriceConfig;
import com.parking.enums.VehicleType;
import com.parking.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriceConfigDAO {

    // [READ] Lấy danh sách
    public List<PriceConfig> getAllPriceConfigs() {
        List<PriceConfig> list = new ArrayList<>();
        String sql = "SELECT * FROM price_config";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                PriceConfig config = new PriceConfig();
                config.setId(rs.getLong("id"));
                config.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                config.setBaseFee(rs.getBigDecimal("base_fee"));
                config.setExtraFeePerHour(rs.getBigDecimal("extra_fee_per_hour"));
                config.setMonthlyPrice(rs.getBigDecimal("monthly_price"));
                list.add(config);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // [CREATE] Thêm mới
    public boolean addPriceConfig(PriceConfig config) {
        String sql = "INSERT INTO price_config (vehicle_type, base_fee, extra_fee_per_hour, monthly_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, config.getVehicleType().name());
            ps.setBigDecimal(2, config.getBaseFee());
            ps.setBigDecimal(3, config.getExtraFeePerHour());
            ps.setBigDecimal(4, config.getMonthlyPrice());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [UPDATE] Cập nhật
    public boolean updatePriceConfig(PriceConfig config) {
        String sql = "UPDATE price_config SET vehicle_type = ?, base_fee = ?, extra_fee_per_hour = ?, monthly_price = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, config.getVehicleType().name());
            ps.setBigDecimal(2, config.getBaseFee());
            ps.setBigDecimal(3, config.getExtraFeePerHour());
            ps.setBigDecimal(4, config.getMonthlyPrice());
            ps.setLong(5, config.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [DELETE] Xóa
    public boolean deletePriceConfig(Long id) {
        String sql = "DELETE FROM price_config WHERE id = ?";
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