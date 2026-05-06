package com.parking.bll;

import com.parking.dao.StaffDAO;
import com.parking.entity.Staff;
import com.parking.enums.UserRole;
import com.parking.utils.PasswordUtil;
import java.util.List;

public class StaffBLL {
    private StaffDAO dao = new StaffDAO();

    public List<Staff> getAll() { return dao.getAllStaff(); }

    public boolean addStaff(String username, String password, String fullName, UserRole role) {
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) return false;
        
        Staff s = new Staff();
        s.setUsername(username);
        s.setFullName(fullName);
        s.setRole(role);
        // TẠO MÃ HASH MẬT KHẨU
        s.setPasswordHash(PasswordUtil.hashPassword(password));
        
        return dao.addStaff(s);
    }

    public boolean updateStaff(Long id, String fullName, UserRole role, String newPassword) {
        if (fullName.isEmpty()) return false;
        
        Staff s = new Staff();
        s.setId(id);
        s.setFullName(fullName);
        s.setRole(role);
        
        boolean updatePwd = newPassword != null && !newPassword.trim().isEmpty();
        if (updatePwd) {
            s.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        }
        
        return dao.updateStaff(s, updatePwd);
    }
    
    public boolean deleteStaff(Long id) { return dao.deleteStaff(id); }
    public Staff login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        // Băm mật khẩu nhập vào để so sánh với chuỗi hash trong database
        String hash = com.parking.utils.PasswordUtil.hashPassword(password);
        return dao.authenticate(username, hash);
    }
}