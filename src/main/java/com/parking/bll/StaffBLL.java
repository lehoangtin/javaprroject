package com.parking.bll;

import com.parking.dao.StaffDAO;
import com.parking.entity.Staff;
import com.parking.utils.Session;
import com.parking.utils.PasswordUtil; // Import class tiện ích mã hóa mật khẩu

import java.util.List;

public class StaffBLL {
    private StaffDAO staffDAO;

    public StaffBLL() {
        this.staffDAO = new StaffDAO();
    }

    // --- Đăng nhập ---
    public Staff login(String username, String rawPassword) {
        // Mã hóa mật khẩu người dùng nhập vào để so sánh với chuỗi hash trong DB[cite: 5]
        String hash = PasswordUtil.hashPassword(rawPassword); 
        return staffDAO.login(username, hash);
    }

    // --- Lấy danh sách ---
    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff(); // Gọi đúng tên hàm trong DAO[cite: 5]
    }

    // --- Thêm nhân viên ---
    public boolean addStaff(Staff staff, String rawPassword) {
        // 1. Phân quyền
        if (!Session.isAdmin()) return false;

        // 2. Validate
        if (staff.getUsername() == null || staff.getUsername().trim().isEmpty() ||
            rawPassword == null || rawPassword.trim().isEmpty() ||
            staff.getFullName() == null || staff.getFullName().trim().isEmpty()) {
            return false;
        }

        // 3. Mã hóa mật khẩu trước khi truyền xuống DAO[cite: 5]
        staff.setPasswordHash(PasswordUtil.hashPassword(rawPassword));

        return staffDAO.addStaff(staff);
    }

    // --- Cập nhật nhân viên ---
    public boolean updateStaff(Staff staff, String rawPassword) {
        // 1. Phân quyền
        if (!Session.isAdmin()) return false;

        // 2. Validate
        if (staff.getId() == null || staff.getId() <= 0 || 
            staff.getUsername() == null || staff.getUsername().trim().isEmpty()) {
            return false;
        }

        // 3. Logic kiểm tra xem có cập nhật mật khẩu hay không[cite: 5]
        boolean updatePassword = false;
        // Nếu người dùng có gõ mật khẩu mới vào ô text thì mới hash và báo DAO cập nhật
        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            staff.setPasswordHash(PasswordUtil.hashPassword(rawPassword));
            updatePassword = true;
        }

        return staffDAO.updateStaff(staff, updatePassword);
    }

    // --- Xóa nhân viên ---
    public boolean deleteStaff(Long id) { // Sửa kiểu int thành Long[cite: 5]
        if (!Session.isAdmin()) return false;

        // Chặn tự xóa chính mình
        if (Session.currentUser != null && Session.currentUser.getId().equals(id)) {
            System.err.println("Từ chối: Không thể tự xóa tài khoản đang đăng nhập!");
            return false;
        }

        return staffDAO.deleteStaff(id);
    }
}