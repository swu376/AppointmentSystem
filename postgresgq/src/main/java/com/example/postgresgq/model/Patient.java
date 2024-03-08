package com.example.postgresgq.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "patient")
public class Patient {
    protected Patient() {}

    public Patient(String name, String email, String contactInfo, String healthInfo, List<Appointment> appointments, User user) {
        this.email = email;
        this.name = name;
        this.contactInfo = contactInfo;
        this.healthInfo = healthInfo;
        this.appointments = appointments;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String contactInfo;
    private String healthInfo;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private User user;

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
