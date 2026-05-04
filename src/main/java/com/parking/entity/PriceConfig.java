package com.parking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRICE_CONFIG")
public class PriceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private ParkingLot parkingLot;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "base_fee")
    private BigDecimal baseFee;

    @Column(name = "extra_fee_per_hour")
    private BigDecimal extraFeePerHour;

    @Column(name = "monthly_price")
    private BigDecimal monthlyPrice;
}