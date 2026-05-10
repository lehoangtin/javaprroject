package com.parking.dao;

import com.parking.entity.Floor;
import com.parking.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FloorDAO {

    // [READ] Lấy danh sách tầng
    public List<Floor> getAllFloors() {
        List<Floor> list = new ArrayList<>();
        String sql = "SELECT * FROM floor ORDER BY floor_number ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Floor floor = new Floor();
                floor.setId(rs.getLong("id"));
                floor.setFloorNumber(rs.getInt("floor_number"));
                floor.setDescription(rs.getString("description"));
                floor.setCapacity(rs.getInt("capacity"));
                list.add(floor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // [CREATE] Thêm tầng mới
    public boolean addFloor(Floor floor) {
    	String sql = "INSERT INTO floor (floor_number, description, capacity) VALUES (?, ?, ?)";
    	try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, floor.getFloorNumber());
            ps.setString(2, floor.getDescription());
            if (floor.getCapacity() != null) {
                ps.setInt(3, floor.getCapacity());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [UPDATE] Cập nhật thông tin tầng
    public boolean updateFloor(Floor floor) {
        // Cập nhật câu lệnh SQL thêm cột capacity
        String sql = "UPDATE floor SET floor_number = ?, description = ?, capacity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, floor.getFloorNumber());
            ps.setString(2, floor.getDescription());
            
            // Xử lý null cho capacity đề phòng trường hợp không nhập
            if (floor.getCapacity() != null) {
                ps.setInt(3, floor.getCapacity());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            ps.setLong(4, floor.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [DELETE] Xóa tầng
    public boolean deleteFloor(Long id) {
        String sql = "DELETE FROM floor WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}