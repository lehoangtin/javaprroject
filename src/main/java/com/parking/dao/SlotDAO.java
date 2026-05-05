package com.parking.dao;

import com.parking.entity.Slot;
import com.parking.enums.SlotStatus; // Import Enum trạng thái
import com.parking.enums.VehicleType; // Import Enum loại xe
import com.parking.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {

    // 1. Lấy danh sách toàn bộ ô đỗ
    public List<Slot> getAll() {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM Slot";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Slot s = new Slot();
                s.setId(rs.getLong("id"));
                s.setFloorId(rs.getLong("floor_id")); // Cập nhật theo Entity mới
                s.setSlotCode(rs.getString("slot_code")); // Đã đổi từ slot_name thành slot_code

                // Chuyển đổi String từ Database sang Enum bằng valueOf()
                if (rs.getString("status") != null) {
                    s.setStatus(SlotStatus.valueOf(rs.getString("status")));
                }
                if (rs.getString("vehicle_type") != null) {
                    s.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                }

                // Lấy ngày giờ cập nhật nếu có
                if (rs.getTimestamp("updated_at") != null) {
                    s.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                
                list.add(s);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi parse Enum: Có dữ liệu trong DB không khớp với Enum định nghĩa!");
        }
        return list;
    }

    // 2. Thêm ô đỗ mới
    public boolean insert(Slot slot) {
        // Cập nhật câu SQL với các trường mới
        String sql = "INSERT INTO Slot (floor_id, slot_code, vehicle_type, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, slot.getFloorId());
            ps.setString(2, slot.getSlotCode());
            
            // Chuyển từ Enum sang String để lưu vào DB
            ps.setString(3, slot.getVehicleType().name());
            ps.setString(4, slot.getStatus() != null ? slot.getStatus().name() : SlotStatus.EMPTY.name());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // 3. Cập nhật trạng thái ô đỗ
    public boolean updateStatus(Long id, SlotStatus status) {
        String sql = "UPDATE Slot SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name()); // Lấy tên chuỗi của Enum
            ps.setLong(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // 4. Xóa ô đỗ
    public boolean delete(Long id) {
        String sql = "DELETE FROM Slot WHERE id = ? AND status = 'EMPTY'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}