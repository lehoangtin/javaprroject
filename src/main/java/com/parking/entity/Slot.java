package com.parking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SLOT")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @Column(name = "slot_code")
    private String slotCode;

    @Column(name = "vehicle_type")
    private String vehicleType;

    private String status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}