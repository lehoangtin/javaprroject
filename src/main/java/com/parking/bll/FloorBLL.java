package com.parking.bll;

import com.parking.dao.FloorDAO;
import com.parking.entity.Floor;
import java.util.List;

public class FloorBLL {
    private FloorDAO floorDAO;

    public FloorBLL() {
        this.floorDAO = new FloorDAO();
    }

    public List<Floor> getAllFloors() {
        return floorDAO.getAllFloors();
    }

    public boolean addFloor(Integer floorNumber, String description) {
        if (floorNumber == null) return false;
        
        Floor floor = new Floor();
        floor.setFloorNumber(floorNumber);
        floor.setDescription(description);
        
        return floorDAO.addFloor(floor);
    }

    public boolean updateFloor(Long id, Integer floorNumber, String description) {
        if (floorNumber == null) return false;

        Floor floor = new Floor();
        floor.setId(id);
        floor.setFloorNumber(floorNumber);
        floor.setDescription(description);
        
        return floorDAO.updateFloor(floor);
    }

    public boolean deleteFloor(Long id) {
        return floorDAO.deleteFloor(id);
    }
}