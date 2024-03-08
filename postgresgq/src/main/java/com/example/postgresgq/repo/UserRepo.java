package com.example.postgresgq.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.postgresgq.model.User;

@RepositoryRestResource
public interface UserRepo extends JpaRepository<User, Long>{
    public User findByUsername(String username);
    public boolean existsByUsername(String username);
}
