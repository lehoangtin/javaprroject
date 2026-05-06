package com.parking.dao;

import com.parking.entity.Staff;
import com.parking.enums.UserRole;
import com.parking.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    public List<Staff> getAllStaff() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Staff s = new Staff();
                s.setId(rs.getLong("id"));
                s.setUsername(rs.getString("username"));
                s.setFullName(rs.getString("full_name"));
                s.setRole(UserRole.valueOf(rs.getString("role")));
                list.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO staff (username, password_hash, full_name, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getUsername());
            ps.setString(2, staff.getPasswordHash()); // Lưu chuỗi đã hash
            ps.setString(3, staff.getFullName());
            ps.setString(4, staff.getRole().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateStaff(Staff staff, boolean updatePassword) {
        String sql = updatePassword ? 
            "UPDATE staff SET full_name = ?, role = ?, password_hash = ? WHERE id = ?" :
            "UPDATE staff SET full_name = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getRole().name());
            if (updatePassword) {
                ps.setString(3, staff.getPasswordHash());
                ps.setLong(4, staff.getId());
            } else {
                ps.setLong(3, staff.getId());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    public boolean deleteStaff(Long id) {
        String sql = "DELETE FROM staff WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    public Staff authenticate(String username, String passwordHash) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Staff s = new Staff();
                    s.setId(rs.getLong("id"));
                    s.setUsername(rs.getString("username"));
                    s.setFullName(rs.getString("full_name"));
                    s.setRole(com.parking.enums.UserRole.valueOf(rs.getString("role")));
                    return s;
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return null;
    }
}