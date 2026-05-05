package com.parking.dao;

import com.parking.entity.ParkingLot;
import com.parking.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotDAO {

    // 1. ĐỌC: Lấy danh sách bãi xe
    public List<ParkingLot> getAll() {
        List<ParkingLot> list = new ArrayList<>();
        String sql = "SELECT * FROM ParkingLot";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ParkingLot p = new ParkingLot();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setAddress(rs.getString("address"));
                p.setPhone(rs.getString("phone"));
                
                int totalFloors = rs.getInt("total_floors");
                if (!rs.wasNull()) p.setTotalFloors(totalFloors);
                
                if (rs.getTimestamp("created_at") != null) {
                    p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. THÊM MỚI
    public boolean insert(ParkingLot p) {
        String sql = "INSERT INTO ParkingLot (name, address, phone, total_floors, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getAddress());
            ps.setString(3, p.getPhone());
            if (p.getTotalFloors() != null) {
                ps.setInt(4, p.getTotalFloors());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // Tự động lấy giờ hiện tại
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 3. CẬP NHẬT
    public boolean update(ParkingLot p) {
        String sql = "UPDATE ParkingLot SET name = ?, address = ?, phone = ?, total_floors = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getAddress());
            ps.setString(3, p.getPhone());
            if (p.getTotalFloors() != null) {
                ps.setInt(4, p.getTotalFloors());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setLong(5, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 4. XÓA
    public boolean delete(Long id) {
        String sql = "DELETE FROM ParkingLot WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}