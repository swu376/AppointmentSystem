package com.example.postgresgq.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.postgresgq.model.Patient;
import com.example.postgresgq.repo.PatientRepo;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    PatientRepo patientRepo;

    @PostMapping("/add") 
    public void add(@RequestBody Patient patient) {
        patientRepo.save(patient);
    }

    @GetMapping("/list")
    public Iterable<Patient> list() {
        return patientRepo.findAll();
    }

    @GetMapping("/get")
    public String get(@RequestParam String Id) {
        return patientRepo.findById(Long.parseLong(Id)).toString();
    }
    
}