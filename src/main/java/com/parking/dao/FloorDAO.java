package com.parking.dao;

import com.parking.entity.Floor;
import com.parking.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FloorDAO {

    public List<Floor> getAll() {
        List<Floor> list = new ArrayList<>();
        String sql = "SELECT * FROM Floor";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Floor f = new Floor();
                f.setId(rs.getLong("id"));
                f.setLotId(rs.getLong("lot_id"));
                f.setFloorNumber(rs.getInt("floor_number"));
                f.setTotalSlots(rs.getInt("total_slots"));
                f.setDescription(rs.getString("description"));
                list.add(f);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Floor f) {
        String sql = "INSERT INTO Floor (lot_id, floor_number, total_slots, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, f.getLotId());
            ps.setInt(2, f.getFloorNumber());
            ps.setInt(3, f.getTotalSlots());
            ps.setString(4, f.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(Floor f) {
        String sql = "UPDATE Floor SET lot_id = ?, floor_number = ?, total_slots = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, f.getLotId());
            ps.setInt(2, f.getFloorNumber());
            ps.setInt(3, f.getTotalSlots());
            ps.setString(4, f.getDescription());
            ps.setLong(5, f.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM Floor WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             // Thêm conn. vào đằng trước prepareStatement
             PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}