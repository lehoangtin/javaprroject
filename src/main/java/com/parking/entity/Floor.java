package com.parking.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "FLOOR")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private ParkingLot parkingLot;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "total_slots")
    private Integer totalSlots;

    private String description;

    @OneToMany(mappedBy = "floor")
    private List<Slot> slots;
}