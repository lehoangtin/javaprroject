package com.parking.dao;

import com.parking.entity.PriceConfig;
import com.parking.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceConfigDAO {

    public List<PriceConfig> getAll() {
        List<PriceConfig> list = new ArrayList<>();
        String sql = "SELECT * FROM Price_Config";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PriceConfig config = new PriceConfig();
                config.setId(rs.getLong("id"));[cite: 14]
                config.setLotId(rs.getLong("lot_id"));[cite: 14]
                config.setVehicleType(rs.getString("vehicle_type")); // Đọc thẳng dạng String
                config.setBaseFee(rs.getBigDecimal("base_fee"));[cite: 14]
                config.setExtraFeePerHour(rs.getBigDecimal("extra_fee_per_hour"));[cite: 14]
                config.setMonthlyPrice(rs.getBigDecimal("monthly_price"));[cite: 14]
                list.add(config);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public boolean insert(PriceConfig config) {
        String sql = "INSERT INTO PriceConfig (lot_id, vehicle_type, base_fee, extra_fee_per_hour, monthly_price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, config.getLotId());[cite: 14]
            ps.setString(2, config.getVehicleType()); // Lưu dạng String
            ps.setBigDecimal(3, config.getBaseFee());[cite: 14]
            ps.setBigDecimal(4, config.getExtraFeePerHour());[cite: 14]
            ps.setBigDecimal(5, config.getMonthlyPrice());[cite: 14]
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    public boolean update(PriceConfig config) {
        String sql = "UPDATE PriceConfig SET lot_id = ?, vehicle_type = ?, base_fee = ?, extra_fee_per_hour = ?, monthly_price = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, config.getLotId());[cite: 14]
            ps.setString(2, config.getVehicleType());[cite: 14]
            ps.setBigDecimal(3, config.getBaseFee());[cite: 14]
            ps.setBigDecimal(4, config.getExtraFeePerHour());[cite: 14]
            ps.setBigDecimal(5, config.getMonthlyPrice());[cite: 14]
            ps.setLong(6, config.getId());[cite: 14]
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM PriceConfig WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}