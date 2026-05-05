package com.parking.entity;
import com.parking.enums.VehicleType;
import java.math.BigDecimal;

public class PriceConfig {
    private Long id;
    private VehicleType vehicleType; 
    private BigDecimal baseFee;          // Giá khởi điểm[cite: 17]
    private BigDecimal extraFeePerHour;  // Phí mỗi giờ tiếp theo[cite: 17]
    private BigDecimal monthlyPrice;     // Giá vé tháng[cite: 17]

    public PriceConfig() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public BigDecimal getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(BigDecimal baseFee) {
		this.baseFee = baseFee;
	}

	public BigDecimal getExtraFeePerHour() {
		return extraFeePerHour;
	}

	public void setExtraFeePerHour(BigDecimal extraFeePerHour) {
		this.extraFeePerHour = extraFeePerHour;
	}

	public BigDecimal getMonthlyPrice() {
		return monthlyPrice;
	}

	public void setMonthlyPrice(BigDecimal monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
}