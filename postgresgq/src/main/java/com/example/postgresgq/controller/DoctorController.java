package com.example.postgresgq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;

import com.example.postgresgq.model.Doctor;
import com.example.postgresgq.model.Hospital;
import com.example.postgresgq.repo.DoctorRepo;
import com.example.postgresgq.repo.HospitalRepo;
import com.example.postgresgq.schema.DoctorSchema.DoctorAddRequest;
import com.example.postgresgq.schema.DoctorSchema.DoctorAddResponse;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private DoctorRepo doctorRepo;
    private HospitalRepo hospitalRepo;

    public DoctorController(DoctorRepo doctorRepo, HospitalRepo hospitalRepo) {
        this.doctorRepo = doctorRepo;
        this.hospitalRepo = hospitalRepo;
    }
    
    @PostMapping("/add")
    public DoctorAddResponse add(@RequestBody DoctorAddRequest req) {
        System.out.println("hello");
        Hospital hospital = hospitalRepo.findByName(req.hospitalName());
        Doctor doctor = new Doctor(
            req.name(), 
            req.email(), 
            null, 
            null, 
            hospital, 
            null
        );
        doctorRepo.save(doctor);
        return new DoctorAddResponse(req.name(), req.hospitalName());
    }
}