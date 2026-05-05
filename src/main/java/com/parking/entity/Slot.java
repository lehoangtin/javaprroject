package com.parking.entity;
import com.parking.enums.*; // Import tất cả enum[cite: 18, 21]
import java.time.LocalDateTime;

public class Slot {
    private Long id;
    private Long floorId;
    private String slotCode;
    private VehicleType vehicleType; // Đã đổi từ String[cite: 21]
    private SlotStatus status;       // Đã đổi từ String[cite: 18]
    private LocalDateTime updatedAt;
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
	public String getSlotCode() {
		return slotCode;
	}
	public void setSlotCode(String slotCode) {
		this.slotCode = slotCode;
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
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}