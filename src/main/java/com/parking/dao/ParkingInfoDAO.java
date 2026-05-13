package com.parking.dao;

import com.parking.entity.ParkingInfo;
import com.parking.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingInfoDAO {
    public ParkingInfo getParkingInfo() {
        String sql = "SELECT * FROM Parking_Info LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            if (rs.next()) {
                ParkingInfo info = new ParkingInfo();
                info.setId(rs.getInt("id"));
                info.setName(rs.getString("name"));
                info.setAddress(rs.getString("address"));
                info.setHotline(rs.getString("hotline"));
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveOrUpdateParkingInfo(ParkingInfo info) {
    	ParkingInfo existingInfo = getParkingInfo();
        
        String sql;
        if (existingInfo == null) {
            sql = "INSERT INTO Parking_Info (name, address, hotline) VALUES (?, ?, ?)";
        } else {
            sql = "UPDATE Parking_Info SET name = ?, address = ?, hotline = ?  WHERE id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, info.getName());
            ps.setString(2, info.getAddress());
            ps.setString(3, info.getHotline());
            
            if (existingInfo != null) {
                ps.setInt(4, existingInfo.getId());
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}