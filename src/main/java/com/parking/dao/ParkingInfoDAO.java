package com.parking.dao;

import com.parking.entity.ParkingInfo;
import com.parking.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingInfoDAO {

    // Lấy thông tin bãi xe (thường là bản ghi đầu tiên - id = 1)
    public ParkingInfo getParkingInfo() {
        String sql = "SELECT * FROM parking_info LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            if (rs.next()) {
                ParkingInfo info = new ParkingInfo();
                info.setId(rs.getInt("id"));
                info.setName(rs.getString("name"));
                info.setAddress(rs.getString("address"));
                info.setHotline(rs.getString("hotline"));
                info.setCapacity(rs.getInt("capacity"));
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lưu hoặc Cập nhật thông tin bãi xe (Upsert)
    public boolean saveOrUpdateParkingInfo(ParkingInfo info) {
        // Kiểm tra xem đã có bản ghi nào chưa
        ParkingInfo existingInfo = getParkingInfo();
        
        String sql;
        if (existingInfo == null) {
            // Chưa có thì Insert
            sql = "INSERT INTO parking_info (name, address, hotline, capacity) VALUES (?, ?, ?, ?)";
        } else {
            // Đã có thì Update bản ghi đó
            sql = "UPDATE parking_info SET name = ?, address = ?, hotline = ?, capacity = ? WHERE id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, info.getName());
            ps.setString(2, info.getAddress());
            ps.setString(3, info.getHotline());
            ps.setInt(4, info.getCapacity());
            
            if (existingInfo != null) {
                ps.setInt(5, existingInfo.getId());
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}