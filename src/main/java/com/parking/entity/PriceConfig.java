package com.parking.entity;
import com.parking.enums.VehicleType;
import java.math.BigDecimal;

public class PriceConfig {
    private Long id;
    private VehicleType vehicleType; 
    private BigDecimal baseFee;
    private BigDecimal extraFeePerHour;
    private BigDecimal monthlyPrice;

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