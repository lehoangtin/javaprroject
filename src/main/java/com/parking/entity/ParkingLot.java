package com.parking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PARKING_LOT")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String address;
    private String phone;

    @Column(name = "total_floors")
    private Integer totalFloors;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "parkingLot")
    private List<Floor> floors;

    @OneToMany(mappedBy = "parkingLot")
    private List<PriceConfig> priceConfigs;
}