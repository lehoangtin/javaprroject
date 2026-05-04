package com.parking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "STAFF")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    private String username;
    private String password;

    private String role;

    @Column(name = "is_active")
    private Boolean isActive;
}