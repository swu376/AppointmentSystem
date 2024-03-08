package com.example.postgresgq.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hospital")
public class Hospital {
    protected Hospital() {}

    public Hospital(String name, String location, HospitalType hospitalType, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.location = location;
        this.hospitalType = hospitalType;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum HospitalType {
        PUBLICHOSPITAL, PRIVATEHOSPITAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalTime openingTime;
    private LocalTime closingTime;

    @Column(unique = true)
    private String name;
    private String location;

    @Enumerated(EnumType.STRING)
    private HospitalType hospitalType;
    private LocalDate onBoarded;

    @OneToMany(mappedBy = "hospital")
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "hospital")
    private List<Appointment> appointments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
