package com.parking.entity;
import com.parking.enums.*;

public class Slot {
    private Long id;
    private Long floorId;      // Liên kết với Floor[cite: 18]
    private String slotNumber; // Số hiệu vị trí (Ví dụ: A-01)
    private VehicleType vehicleType; // Loại xe cho phép[cite: 18]
    private SlotStatus status;       //[cite: 18]
    public Slot() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public SlotStatus getStatus() {
		return status;
	}

	public void setStatus(SlotStatus status) {
		this.status = status;
	}
}