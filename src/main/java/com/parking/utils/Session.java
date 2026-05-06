package com.parking.utils;

import com.parking.entity.Staff;

public class Session {
    // Biến static lưu trữ thông tin tài khoản đang đăng nhập
    public static Staff currentUser = null;
    
    // Hàm kiểm tra xem người dùng hiện tại có phải là Admin không
    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole().toString().equals("ADMIN");
        // Lưu ý: Sửa chữ "ADMIN" cho khớp với enum UserRole của bạn nhé.
    }
}