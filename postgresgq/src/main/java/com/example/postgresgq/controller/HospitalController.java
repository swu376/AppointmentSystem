package com.example.postgresgq.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.postgresgq.model.Hospital;
import com.example.postgresgq.model.Hospital.HospitalType;
import com.example.postgresgq.repo.HospitalRepo;
import com.example.postgresgq.schema.HospitalSchema.HospitalAddRequest;
import com.example.postgresgq.schema.HospitalSchema.HospitalResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/hospital")
public class HospitalController {
    @Autowired
    HospitalRepo hospitalRepo; 

    @PostMapping("/add")
    public void add(@RequestBody HospitalAddRequest hospital) {
        Hospital newHospital = new Hospital(
            hospital.name(), 
            hospital.location(),
            HospitalType.PUBLICHOSPITAL,
            LocalTime.parse(hospital.openTime()),
            LocalTime.parse(hospital.closeTime())
        );
        hospitalRepo.save(newHospital);
    }
    @GetMapping("/list")
    public Iterable<HospitalResponse> list() {
        List<Hospital> hospitals = hospitalRepo.findAll();
        List<HospitalResponse> response = hospitals
            .stream()
            .map(hospital -> new HospitalResponse(
                hospital.getName(), 
                hospital.getLocation()
            ))
            .toList();
        return response;
    }
} 

