package com.parking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "VEHICLE")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "plate_number")
    private String plateNumber;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "owner_phone")
    private String ownerPhone;
}