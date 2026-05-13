package com.parking.bll;

import com.parking.dao.StaffDAO;
import com.parking.entity.Staff;
import com.parking.utils.Session;
import com.parking.utils.PasswordUtil; 

import java.util.List;

public class StaffBLL {
    private StaffDAO staffDAO;

    public StaffBLL() {
        this.staffDAO = new StaffDAO();
    }

    public Staff login(String username, String rawPassword) {
        String hash = PasswordUtil.hashPassword(rawPassword); 
        return staffDAO.login(username, hash);
    }

    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff();
    }

    public boolean addStaff(Staff staff, String rawPassword) {
        if (!Session.isAdmin()) return false;

        if (staff.getUsername() == null || staff.getUsername().trim().isEmpty() ||
            rawPassword == null || rawPassword.trim().isEmpty() ||
            staff.getFullName() == null || staff.getFullName().trim().isEmpty()) {
            return false;
        }

        staff.setPasswordHash(PasswordUtil.hashPassword(rawPassword));
        return staffDAO.addStaff(staff);
    }

    public boolean updateStaff(Staff staff, String rawPassword) {
        if (!Session.isAdmin()) return false;

        if (staff.getId() == null || staff.getId() <= 0 || 
            staff.getUsername() == null || staff.getUsername().trim().isEmpty()) {
            return false;
        }

        boolean updatePassword = false;
        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            staff.setPasswordHash(PasswordUtil.hashPassword(rawPassword));
            updatePassword = true;
        }

        return staffDAO.updateStaff(staff, updatePassword);
    }

    // Thay đổi kiểu trả về thành String để báo lỗi chi tiết
    public String deleteStaff(Long id, String username) { 
        if (!Session.isAdmin()) return "Bạn không có quyền thực hiện thao tác này!";

        if ("admin".equalsIgnoreCase(username)) {
            return "Hệ thống từ chối: Không thể vô hiệu hóa tài khoản Admin gốc!";
        }

        if (Session.currentUser != null && Session.currentUser.getId().equals(id)) {
            return "Từ chối: Không thể tự vô hiệu hóa tài khoản đang đăng nhập!";
        }

        boolean success = staffDAO.deleteStaff(id);
        return success ? "SUCCESS" : "Lỗi cơ sở dữ liệu khi cập nhật trạng thái!";
    }
}