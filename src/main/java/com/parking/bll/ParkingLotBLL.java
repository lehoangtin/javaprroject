package com.parking.bll;

import com.parking.dao.ParkingLotDAO;
import com.parking.entity.ParkingInfo;
import java.util.List;

public class ParkingLotBLL {
    private ParkingLotDAO parkingLotDAO = new ParkingLotDAO();

    public List<ParkingInfo> getAllParkingLots() {
        return parkingLotDAO.getAll();
    }

    public boolean saveParkingLot(ParkingInfo p) {
        if (p.getName() == null || p.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên bãi đỗ xe không được để trống!");
        }
        if (p.getTotalFloors() != null && p.getTotalFloors() <= 0) {
            throw new IllegalArgumentException("Tổng số tầng phải lớn hơn 0!");
        }

        if (p.getId() == null || p.getId() == 0) {
            return parkingLotDAO.insert(p);
        } else {
            return parkingLotDAO.update(p);
        }
    }

    public boolean deleteParkingLot(Long id) {
        if (id == null) throw new IllegalArgumentException("Không tìm thấy bãi đỗ xe cần xóa!");
        // Ở thực tế, bạn có thể gọi FloorDAO để check xem bãi này có tầng nào chưa trước khi cho xóa
        return parkingLotDAO.delete(id);
    }
}