package com.example.postgresgq.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.postgresgq.model.Hospital;

@RepositoryRestResource
public interface HospitalRepo extends JpaRepository<Hospital, Long>{
    public Hospital findByLocation(String location);
    public Hospital findByName(String name);
}
