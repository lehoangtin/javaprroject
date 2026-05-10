package com.parking.dao;

import com.parking.entity.Vehicle;
import com.parking.enums.VehicleType;
import com.parking.utils.DBConnection;
import java.sql.*;

public class VehicleDAO {
	public com.parking.entity.Vehicle findById(Long id) {
        String sql = "SELECT * FROM vehicle WHERE id = ?";
        try (java.sql.Connection conn = com.parking.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    com.parking.entity.Vehicle v = new com.parking.entity.Vehicle();
                    v.setId(rs.getLong("id"));
                    v.setLicensePlate(rs.getString("license_plate"));
                    v.setVehicleType(com.parking.enums.VehicleType.valueOf(rs.getString("vehicle_type")));
                    return v;
                }
            }
        } catch (java.sql.SQLException e) { e.printStackTrace(); }
        return null;
    }
    // Tìm xe theo biển số
    public Vehicle findByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM vehicle WHERE license_plate = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehicle v = new Vehicle();
                    v.setId(rs.getLong("id"));
                    v.setLicensePlate(rs.getString("license_plate")); // Khớp license_plate trong DB
                    v.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                    return v;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Thêm xe mới (khách vãng lai) và trả về ID vừa tạo
    public Long addVehicle(Vehicle v) {
        String sql = "INSERT INTO vehicle (license_plate, vehicle_type) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             // Chú ý tham số Statement.RETURN_GENERATED_KEYS để lấy ID vừa tạo
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getLicensePlate());
            ps.setString(2, v.getVehicleType().name());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}