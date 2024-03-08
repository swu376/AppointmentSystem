package com.example.postgresgq.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.postgresgq.model.Appointment;
import com.example.postgresgq.model.Doctor;
import com.example.postgresgq.model.Hospital;
import com.example.postgresgq.model.Patient;
import com.example.postgresgq.model.User;
import com.example.postgresgq.model.Appointment.AppointmentStatus;
import com.example.postgresgq.model.Appointment.AppointmentType;
import com.example.postgresgq.repo.AppointmentRepo;
import com.example.postgresgq.repo.DoctorRepo;
import com.example.postgresgq.repo.HospitalRepo;
import com.example.postgresgq.repo.UserRepo;
import com.example.postgresgq.schema.AppointmentSchema.AppointmentGetResponse;
import com.example.postgresgq.schema.AppointmentSchema.AppointmentUpdateRequest;
import com.example.postgresgq.schema.AppointmentSchema.AppointmentUpdateResponse;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final HospitalRepo hospitalRepo;
    private final UserRepo userRepo;

    public AppointmentController(
        AppointmentRepo appointmentRepo, 
        DoctorRepo doctorRepo, 
        HospitalRepo hospitalRepo,
        UserRepo userRepo
    ) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.hospitalRepo = hospitalRepo;
        this.userRepo = userRepo;
    }
    private void genAppointmentsByHospitalAndDoctor(LocalDate date, Hospital hospital, Doctor doctor) {
        if (appointmentRepo.existsByAppointmentDateBetweenAndDoctor(
            date.atStartOfDay(), 
            date.atTime(LocalTime.MAX), 
            doctor
        )) return;
        LocalTime openTime = hospital.getOpeningTime();
        LocalTime closeTime = hospital.getClosingTime();
        List<Appointment> appointments = new ArrayList<>();

        while(openTime.isBefore(closeTime)) {
            LocalDateTime startTime = date.atTime(openTime);
            Appointment appointment = new Appointment(
                startTime, 
                AppointmentType.INPERSON, 
                AppointmentStatus.AVAILABLE, 
                hospital, 
                doctor, 
                null);
            appointments.add(appointment);
            openTime = openTime.plusMinutes(30);
        }
        appointmentRepo.saveAll(appointments);
    }

    private void genAppointmentsByHospital(LocalDate date, String hospitalName) {
        Hospital hospital = hospitalRepo.findByName(hospitalName);
        List<Doctor> doctors = doctorRepo.findByHospital(hospital); 
        for(Doctor doctor : doctors) {
            genAppointmentsByHospitalAndDoctor(date, hospital, doctor);
        }
    }
    
    @GetMapping("/list")
    @Transactional
    public List<AppointmentGetResponse> list(
        @RequestParam(name = "date", required = true) String dateString, 
        @RequestParam(name = "hospital", required = true) String hospitalName
    ) {
        LocalDate date = LocalDate.parse(dateString);
        genAppointmentsByHospital(date, hospitalName);

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        ArrayList<Appointment> appointments = new ArrayList<>(
            appointmentRepo.findByAppointmentDateAndHospitalNameAndAppointmentStatus(
                startOfDay, 
                endOfDay,
                hospitalName,
                AppointmentStatus.AVAILABLE
            ));
        List<AppointmentGetResponse> response = new ArrayList<>();
        for(Appointment appointment : appointments) {
            response.add(new AppointmentGetResponse(
                appointment.getHospital().getName(),
                appointment.getAppointmentDate(), 
                appointment.getAppointmentDate().plusMinutes(30),
                appointment.getDoctor().getName(),
                appointment.getDoctor().getId(),
                appointment.getId()
            ));
        }

        return response;
    }

    @GetMapping("/history")
    public List<AppointmentGetResponse> history() {
        String name = (String) RequestContextHolder
            .getRequestAttributes()
            .getAttribute("username", RequestAttributes.SCOPE_REQUEST);
        User user = userRepo.findByUsername(name);
        Patient patient = user.getPatient();
        List<AppointmentGetResponse> response = new ArrayList<>();
        if (patient == null) {
            return response;
        }
        List<Appointment> appointments = appointmentRepo.findByPatient(patient);
        for(Appointment appointment : appointments) {
            response.add(new AppointmentGetResponse(
                appointment.getHospital().getName(),
                appointment.getAppointmentDate(), 
                appointment.getAppointmentDate().plusMinutes(30),
                appointment.getDoctor().getName(),
                appointment.getDoctor().getId(),
                appointment.getId()
            ));
        }
        return response;
    }
        
    @PostMapping("/delete")
    public ResponseEntity<AppointmentUpdateResponse> delete(@RequestBody AppointmentUpdateRequest req) {
        String name = (String) RequestContextHolder
            .getRequestAttributes()
            .getAttribute("username", RequestAttributes.SCOPE_REQUEST);
        User user = userRepo.findByUsername(name);
        Patient patient = user.getPatient(); 
        Appointment appointment = appointmentRepo.findById(req.appointmentId()).orElseThrow();
        if (patient == null || !appointment.getPatient().equals(patient)) {
            return ResponseEntity.badRequest().body(
                new AppointmentUpdateResponse("Patient is null or patient is not the owner of the appointment")
            );
        }
        appointment.setPatient(null);
        appointment.setAppointmentStatus(AppointmentStatus.AVAILABLE);
        appointmentRepo.save(appointment);
        
        return ResponseEntity.ok(new AppointmentUpdateResponse("Success"));
    }

    @PostMapping("/update")
    @Transactional
    public ResponseEntity<AppointmentUpdateResponse> update(@RequestBody AppointmentUpdateRequest request) {
        Appointment appointment = appointmentRepo.findById(request.appointmentId()).orElseThrow();
        String name = (String) RequestContextHolder
            .getRequestAttributes()
            .getAttribute("username", RequestAttributes.SCOPE_REQUEST);
        User user = userRepo.findByUsername(name);
        Patient patient = user.getPatient(); 
        if (patient == null) {
            return ResponseEntity.badRequest().body(
                new AppointmentUpdateResponse("Error: user is not a patient")
            );
        }
        appointment.setPatient(patient);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        return ResponseEntity.ok(new AppointmentUpdateResponse("Success"));
    }
}