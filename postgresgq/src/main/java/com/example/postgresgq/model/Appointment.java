package com.example.postgresgq.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

// define a enum for type which has values of "public" and "private"

@Data
@Entity
@Table(name = "appointment")
public class Appointment {
    protected Appointment() {}

    public Appointment(LocalDateTime appointmentDate, AppointmentType appointmentType, AppointmentStatus appointmentStatus, Hospital hospital, Doctor doctor, Patient patient) {
        this.appointmentDate = appointmentDate;
        this.appointmentType = appointmentType;
        this.appointmentStatus = appointmentStatus;
        this.hospital = hospital;
        this.doctor = doctor;
        this.patient = patient;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum AppointmentType {
        INPERSON, VIRTUAL
    }

    public enum AppointmentStatus {
        AVAILABLE, SCHEDULED, CANCELLED, COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id;
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private AppointmentType appointmentType;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

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
