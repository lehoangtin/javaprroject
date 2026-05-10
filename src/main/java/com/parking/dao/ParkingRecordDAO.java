package com.parking.dao;

import com.parking.entity.ParkingRecord;
import com.parking.utils.DBConnection;
import java.sql.*;

public class ParkingRecordDAO {
	// Lấy phiếu đỗ của xe ĐANG TRONG BÃI dựa vào biển số
    public com.parking.entity.ParkingRecord findActiveRecordByLicensePlate(String licensePlate) {
        String sql = "SELECT pr.* FROM parking_record pr " +
                     "JOIN vehicle v ON pr.vehicle_id = v.id " +
                     "WHERE v.license_plate = ? AND pr.status = 'IN_PARKING' LIMIT 1";
        try (java.sql.Connection conn = com.parking.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    com.parking.entity.ParkingRecord record = new com.parking.entity.ParkingRecord();
                    record.setId(rs.getLong("id"));
                    record.setTimeIn(rs.getTimestamp("time_in").toLocalDateTime());
                    record.setVehicleId(rs.getLong("vehicle_id"));
                    record.setSlotId(rs.getLong("slot_id"));
                    record.setStaffId(rs.getLong("staff_id"));
                    return record;
                }
            }
        } catch (java.sql.SQLException e) { e.printStackTrace(); }
        return null;
    }
    // Cập nhật Giờ ra, Phí, và Trạng thái khi xe check-out
    public boolean updateRecordOnCheckout(com.parking.entity.ParkingRecord record) {
        String sql = "UPDATE parking_record SET time_out = ?, fee = ?, status = ? WHERE id = ?";
        try (java.sql.Connection conn = com.parking.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(record.getTimeOut()));
            ps.setBigDecimal(2, record.getFee());
            ps.setString(3, record.getStatus().name());
            ps.setLong(4, record.getId());
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    // Hàm lưu giao dịch lúc xe vào
    public boolean insertRecord(ParkingRecord record) {
        String sql = "INSERT INTO parking_record (time_in, vehicle_id, slot_id, staff_id, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            // Chuyển từ LocalDateTime của Java sang Timestamp của SQL
            ps.setTimestamp(1, Timestamp.valueOf(record.getTimeIn()));
            ps.setLong(2, record.getVehicleId());
            ps.setLong(3, record.getSlotId());
            ps.setLong(4, record.getStaffId());
            ps.setString(5, record.getStatus().name());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return false;
    }
}
