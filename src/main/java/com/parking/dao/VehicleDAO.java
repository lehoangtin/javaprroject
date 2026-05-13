package com.parking.dao;

import com.parking.entity.Vehicle;
import com.parking.enums.VehicleType;
import com.parking.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    
    // 1. LẤY XE THEO ID
    public Vehicle findById(Long id) {
        String sql = "SELECT * FROM vehicle WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehicle v = new Vehicle();
                    v.setId(rs.getLong("id"));
                    v.setLicensePlate(rs.getString("license_plate"));
                    v.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                    v.setOwnerName(rs.getString("owner_name"));   
                    v.setOwnerPhone(rs.getString("owner_phone")); 
                    return v;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 2. TÌM XE THEO BIỂN SỐ
    public Vehicle findByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM vehicle WHERE license_plate = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehicle v = new Vehicle();
                    v.setId(rs.getLong("id"));
                    v.setLicensePlate(rs.getString("license_plate")); 
                    v.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                    v.setOwnerName(rs.getString("owner_name"));
                    v.setOwnerPhone(rs.getString("owner_phone"));
                    return v;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 3. LẤY TOÀN BỘ DANH SÁCH XE (MỚI THÊM)
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setId(rs.getLong("id"));
                v.setLicensePlate(rs.getString("license_plate"));
                v.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                v.setOwnerName(rs.getString("owner_name"));
                v.setOwnerPhone(rs.getString("owner_phone"));
                list.add(v);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 4. THÊM XE MỚI
    public Long addVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicle (license_plate, vehicle_type, owner_name, owner_phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getLicensePlate());
            ps.setString(2, v.getVehicleType().name());
            ps.setString(3, v.getOwnerName());
            ps.setString(4, v.getOwnerPhone());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 5. CẬP NHẬT TOÀN BỘ THÔNG TIN XE (MỚI THÊM)
    public boolean updateVehicle(Vehicle v) {
        String sql = "UPDATE vehicle SET license_plate = ?, vehicle_type = ?, owner_name = ?, owner_phone = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getLicensePlate());
            ps.setString(2, v.getVehicleType().name());
            ps.setString(3, v.getOwnerName());
            ps.setString(4, v.getOwnerPhone());
            ps.setLong(5, v.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 6. CẬP NHẬT RIÊNG THÔNG TIN CHỦ XE
    public boolean updateOwnerInfo(Long vehicleId, String ownerName, String ownerPhone) {
        String sql = "UPDATE vehicle SET owner_name = ?, owner_phone = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerName);
            ps.setString(2, ownerPhone);
            ps.setLong(3, vehicleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 7. XÓA XE (MỚI THÊM)
    public boolean deleteVehicle(Long id) {
        String sql = "DELETE FROM vehicle WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}