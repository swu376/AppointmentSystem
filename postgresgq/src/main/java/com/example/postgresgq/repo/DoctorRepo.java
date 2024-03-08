package com.example.postgresgq.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.postgresgq.model.Doctor;
import java.util.List;
import com.example.postgresgq.model.Hospital;


@RepositoryRestResource
public interface DoctorRepo extends JpaRepository<Doctor, Long>{
    public List<Doctor> findByHospital(Hospital hospital);
}