package com.parking.dao;

import com.parking.entity.ParkingRecord;
import com.parking.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingRecordDAO {
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
    
    public boolean insertRecord(ParkingRecord record) {
        String sql = "INSERT INTO parking_record (time_in, vehicle_id, slot_id, staff_id, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
    public List<Map<String, Object>> getTransactionHistory() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT r.id, v.license_plate, v.vehicle_type, s.slot_number, " +
                     "r.time_in, r.time_out, r.fee, st.full_name as staff_name, r.status " +
                     "FROM parking_record r " +
                     "JOIN Vehicle v ON r.vehicle_id = v.id " +
                     "JOIN Slot s ON r.slot_id = s.id " +
                     "LEFT JOIN Staff st ON r.staff_id = st.id " +
                     "ORDER BY r.time_in DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getLong("id"));
                row.put("license_plate", rs.getString("license_plate"));
                row.put("vehicle_type", rs.getString("vehicle_type"));
                row.put("slot_number", rs.getString("slot_number"));
                row.put("time_in", rs.getTimestamp("time_in"));
                row.put("time_out", rs.getTimestamp("time_out"));
                row.put("fee", rs.getBigDecimal("fee"));
                row.put("staff_name", rs.getString("staff_name"));
                row.put("status", rs.getString("status"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Map<String, String> getVehicleInfoInSlot(String slotNumber) {
        String sql = "SELECT v.license_plate, v.vehicle_type, v.owner_name, r.time_in " +
                     "FROM parking_record r " +
                     "JOIN Vehicle v ON r.vehicle_id = v.id " +
                     "JOIN Slot s ON r.slot_id = s.id " +
                     "WHERE s.slot_number = ? AND r.status = 'IN_PARKING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slotNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, String> info = new HashMap<>();
                info.put("plate", rs.getString("license_plate"));
                info.put("type", rs.getString("vehicle_type"));
                info.put("owner", rs.getString("owner_name"));
                info.put("timeIn", rs.getString("time_in"));
                return info;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
}
