package com.parking.dao;

import com.parking.entity.ParkingRecord;
import com.parking.enums.RecordStatus; // Import Enum
import com.parking.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingRecordDAO {

    // 1. NHẬN XE: Thêm record mới vào DB
    public boolean insert(ParkingRecord record) {
        String sql = "INSERT INTO ParkingRecord (time_in, vehicle_id, slot_id, ticket_id, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(record.getTimeIn()));
            ps.setLong(2, record.getVehicleId());
            ps.setLong(3, record.getSlotId());
            
            // Xử lý vé tháng (nếu có)
            if (record.getTicketId() != null && record.getTicketId() > 0) {
                ps.setLong(4, record.getTicketId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            
            ps.setString(5, record.getStatus().name()); // Lưu Enum dưới dạng String
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. TRẢ XE: Cập nhật giờ ra, tiền phí và đổi trạng thái
    public boolean updateCheckOut(ParkingRecord record) {
        String sql = "UPDATE ParkingRecord SET time_out = ?, fee = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(record.getTimeOut()));
            ps.setBigDecimal(2, record.getFee());
            ps.setString(3, record.getStatus().name());
            ps.setLong(4, record.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. LẤY DANH SÁCH XE ĐANG TRONG BÃI (Dựa theo status)
    public List<ParkingRecord> getActiveRecords() {
        List<ParkingRecord> list = new ArrayList<>();
        // Giả sử trạng thái đang đỗ là ACTIVE
        String sql = "SELECT * FROM ParkingRecord WHERE status = 'ACTIVE' ORDER BY time_in DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ParkingRecord pr = new ParkingRecord();
                pr.setId(rs.getLong("id"));
                pr.setTimeIn(rs.getTimestamp("time_in").toLocalDateTime());
                pr.setVehicleId(rs.getLong("vehicle_id"));
                pr.setSlotId(rs.getLong("slot_id"));
                
                long ticketId = rs.getLong("ticket_id");
                if (!rs.wasNull()) pr.setTicketId(ticketId);
                
                if (rs.getString("status") != null) {
                    pr.setStatus(RecordStatus.valueOf(rs.getString("status")));
                }
                list.add(pr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}