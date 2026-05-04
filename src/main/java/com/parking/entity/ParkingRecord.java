package com.parking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PARKING_RECORD")
public class ParkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "plate_number")
    private String plateNumber;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "time_in")
    private LocalDateTime timeIn;

    @Column(name = "time_out")
    private LocalDateTime timeOut;

    private BigDecimal fee;

    @Column(name = "payment_status")
    private String paymentStatus;
}