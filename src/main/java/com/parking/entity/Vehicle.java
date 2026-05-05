package com.parking.entity;
import com.parking.enums.VehicleType; // Import enum vừa tạo[cite: 21]

public class Vehicle {
    private Long id;
    private String plateNumber;
    private VehicleType vehicleType; // Đã đổi từ String sang Enum[cite: 21]
    private String ownerName;
    private String ownerPhone;

    // Getter/Setter cho vehicleType dùng kiểu VehicleType
    public VehicleType getVehicleType() { return vehicleType; }
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerPhone() {
		return ownerPhone;
	}
	public void setOwnerPhone(String ownerPhone) {
		this.ownerPhone = ownerPhone;
	}
	public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
}