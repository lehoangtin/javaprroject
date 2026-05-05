package com.parking.dao;

import com.parking.entity.Staff;
import com.parking.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    // 1. ĐỌC: Lấy danh sách
    public List<Staff> getAll() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Staff s = new Staff();
                s.setId(rs.getLong("id"));
                s.setFullName(rs.getString("full_name"));
                s.setUsername(rs.getString("username"));
                s.setRole(rs.getString("role"));
                s.setIsActive(rs.getBoolean("is_active"));
                list.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. THÊM MỚI
    public boolean insert(Staff s) {
        String sql = "INSERT INTO Staff (full_name, username, password_hash, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getUsername());
            ps.setString(3, s.getPassword());
            ps.setString(4, s.getRole());
            ps.setBoolean(5, s.getIsActive());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 3. CẬP NHẬT (SỬA)
    public boolean update(Staff s) {
        String sql = "UPDATE Staff SET full_name = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getRole());
            ps.setLong(3, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 4. KHÓA / MỞ KHÓA (SOFT DELETE)
    public boolean toggleStatus(Long id, boolean status) {
        String sql = "UPDATE Staff SET is_active = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Xử lý Login
    public Staff login(String username, String passwordHash) {
        String sql = "SELECT * FROM Staff WHERE username = ? AND password_hash = ? AND is_active = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Staff s = new Staff();
                s.setId(rs.getLong("id"));
                s.setFullName(rs.getString("full_name"));
                s.setUsername(rs.getString("username"));
                s.setRole(rs.getString("role"));
                s.setIsActive(rs.getBoolean("is_active"));
                return s;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}