package com.parking.entity;

import java.math.BigDecimal;

public class PriceConfig {
    private Long id; // Chuyển sang Long cho khớp BIGINT
    private Long lotId; 
    private String vehicleType;
    private BigDecimal baseFee;
    private BigDecimal extraFeePerHour;
    private BigDecimal monthlyPrice;

    public PriceConfig() {}

    // --- Getter và Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public BigDecimal getBaseFee() { return baseFee; }
    public void setBaseFee(BigDecimal baseFee) { this.baseFee = baseFee; }

    public BigDecimal getExtraFeePerHour() { return extraFeePerHour; }
    public void setExtraFeePerHour(BigDecimal extraFeePerHour) { this.extraFeePerHour = extraFeePerHour; }

    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }
}