package com.parking.bll;

import com.parking.dao.StaffDAO;
import com.parking.entity.Staff;
import com.parking.utils.PasswordUtil;
import java.util.List;

public class StaffBLL {
    private StaffDAO staffDAO = new StaffDAO();

    public List<Staff> getAllStaffs() { return staffDAO.getAll(); }

    public Staff login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
        }
        String hashedPass = PasswordUtil.hashPassword(password);
        return staffDAO.login(username, hashedPass);
    }

    // Xử lý logic Thêm nhân viên
    public boolean addStaff(Staff s) {
        if (s.getFullName().trim().isEmpty() || s.getUsername().trim().isEmpty() || s.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
        }
        if (s.getPassword().length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự!");
        }
        
        // Hash mật khẩu và set trạng thái mặc định
        s.setPassword(PasswordUtil.hashPassword(s.getPassword()));
        s.setIsActive(true); 
        
        return staffDAO.insert(s);
    }

    // Xử lý logic Cập nhật thông tin
    public boolean updateStaff(Staff s) {
        if (s.getId() == null) throw new IllegalArgumentException("Không tìm thấy nhân viên!");
        if (s.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống!");
        }
        return staffDAO.update(s);
    }

    public boolean toggleStatus(Long id, boolean currentStatus) {
        if (id == null) return false;
        return staffDAO.toggleStatus(id, !currentStatus);
    }
}