package com.parking.bll;

import com.parking.dao.ParkingInfoDAO;
import com.parking.entity.ParkingInfo;

public class ParkingInfoBLL {
    private ParkingInfoDAO dao;

    public ParkingInfoBLL() {
        this.dao = new ParkingInfoDAO();
    }

    public ParkingInfo getInfo() {
        return dao.getParkingInfo();
    }

    public boolean saveInfo(String name, String address, String hotline, Integer capacity) {
        // Ràng buộc cơ bản
        if (name == null || name.trim().isEmpty() || capacity == null || capacity <= 0) {
            return false;
        }

        ParkingInfo info = new ParkingInfo();
        info.setName(name);
        info.setAddress(address);
        info.setHotline(hotline);
        info.setCapacity(capacity);

        return dao.saveOrUpdateParkingInfo(info);
    }
}