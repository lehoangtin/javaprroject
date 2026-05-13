package com.parking.utils;

import com.parking.entity.Staff;

public class Session {
    public static Staff currentUser = null;
        public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole().toString().equals("ADMIN");
        }
}