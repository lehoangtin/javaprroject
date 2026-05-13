package com.parking.dao;

import com.parking.entity.Slot;
import com.parking.enums.SlotStatus;
import com.parking.enums.VehicleType;
import com.parking.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {

    public List<Slot> getAllSlots() {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM slot ORDER BY floor_id, slot_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Slot slot = new Slot();
                slot.setId(rs.getLong("id"));
                slot.setFloorId(rs.getLong("floor_id"));
                slot.setSlotNumber(rs.getString("slot_number"));
                slot.setVehicleType(VehicleType.valueOf(rs.getString("vehicle_type")));
                slot.setStatus(SlotStatus.valueOf(rs.getString("status")));
                list.add(slot);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public int countTotalSlots() {
        String sql = "SELECT COUNT(*) FROM slot";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean addSlot(Slot slot) {
        String sql = "INSERT INTO slot (floor_id, slot_number, vehicle_type, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, slot.getFloorId());
            ps.setString(2, slot.getSlotNumber());
            ps.setString(3, slot.getVehicleType().name());
            ps.setString(4, slot.getStatus().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateSlot(Slot slot) {
        String sql = "UPDATE slot SET floor_id = ?, slot_number = ?, vehicle_type = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, slot.getFloorId());
            ps.setString(2, slot.getSlotNumber());
            ps.setString(3, slot.getVehicleType().name());
            ps.setString(4, slot.getStatus().name());
            ps.setLong(5, slot.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean deleteSlot(Long id) {
        String sql = "DELETE FROM slot WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    public List<Slot> getSlotsByFloor(Long floorId) {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM slot WHERE floor_id = ?"; 
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setLong(1, floorId); 
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Slot slot = new Slot();
                    slot.setId(rs.getLong("id"));
                    slot.setFloorId(rs.getLong("floor_id"));
                    slot.setSlotNumber(rs.getString("slot_number"));
                    
                    slot.setVehicleType(com.parking.enums.VehicleType.valueOf(rs.getString("vehicle_type")));
                    slot.setStatus(com.parking.enums.SlotStatus.valueOf(rs.getString("status")));
                    
                    list.add(slot);
                }
            }
        } catch (SQLException e) { 
            System.err.println("Lỗi truy vấn danh sách Slot theo Tầng:");
            e.printStackTrace(); 
        }
        return list;
    }
    public boolean updateStatus(Long slotId, com.parking.enums.SlotStatus newStatus) {
        String sql = "UPDATE slot SET status = ? WHERE id = ?";
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, newStatus.name());
            ps.setLong(2, slotId);
            
            return ps.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            System.err.println("Lỗi khi cập nhật trạng thái Slot:");
            e.printStackTrace();
        }
        return false;
    }
}