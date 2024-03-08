package com.example.postgresgq.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.postgresgq.model.Patient;

@RepositoryRestResource
public interface PatientRepo extends JpaRepository<Patient, Long>{
}