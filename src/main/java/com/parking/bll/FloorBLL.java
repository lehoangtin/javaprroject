package com.parking.bll;

import com.parking.dao.FloorDAO;
import com.parking.entity.Floor;
import java.util.List;

public class FloorBLL {
    private FloorDAO floorDAO = new FloorDAO();

    public List<Floor> getAllFloors() {
        return floorDAO.getAll();
    }

    public boolean saveFloor(Floor f) {
        if (f.getLotId() == null) {
            throw new IllegalArgumentException("Vui lòng xác định mã bãi đỗ xe trực thuộc!");
        }
        if (f.getFloorNumber() == null || f.getFloorNumber() < 0) {
            throw new IllegalArgumentException("Số tầng không hợp lệ (Phải là số >= 0)!");
        }
        if (f.getTotalSlots() == null || f.getTotalSlots() <= 0) {
            throw new IllegalArgumentException("Tổng số chỗ đỗ phải lớn hơn 0!");
        }
        
        if (f.getId() == null || f.getId() == 0) {
            return floorDAO.insert(f);
        } else {
            return floorDAO.update(f);
        }
    }

    public boolean deleteFloor(Long id) {
        if (id == null) throw new IllegalArgumentException("Lỗi: Không tìm thấy ID tầng!");
        // Ràng buộc bổ sung sau này: Gọi SlotDAO kiểm tra xem tầng này có ô đỗ nào không, nếu có thì chặn xóa.
        return floorDAO.delete(id);
    }
}