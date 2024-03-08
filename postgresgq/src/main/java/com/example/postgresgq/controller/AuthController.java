package com.example.postgresgq.controller;


import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.example.postgresgq.config.UserDetailsImpl;
import com.example.postgresgq.config.jwt.JwtUtils;
import com.example.postgresgq.model.Patient;
import com.example.postgresgq.model.User;
import com.example.postgresgq.model.User.Role;
import com.example.postgresgq.repo.PatientRepo;
import com.example.postgresgq.repo.UserRepo;
import com.example.postgresgq.schema.AuthSchema.LoginRequest;
import com.example.postgresgq.schema.AuthSchema.LoginResponse;
import com.example.postgresgq.schema.AuthSchema.SignupRequest;
import com.example.postgresgq.schema.AuthSchema.SignupResponse;



import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private PatientRepo patientRepo;
    private AuthenticationManager authenticationManager;
    private UserRepo userRepo;
    private JwtUtils jwtUtils;
    private PasswordEncoder encoder;

    public AuthController(
        AuthenticationManager authenticationManager, 
        UserRepo userRepo, 
        JwtUtils jwtUtils,
        PasswordEncoder encoder,
        PatientRepo patientRepo
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
        this.patientRepo = patientRepo;
    }

    @GetMapping("/checkToken")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Boolean checkToken() {
        String name = (String) RequestContextHolder
            .getRequestAttributes()
            .getAttribute("username", RequestAttributes.SCOPE_REQUEST);
        boolean exists = userRepo.existsByUsername(name);
        return userRepo.existsByUsername(name);
    }
    

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest signin) {
        System.out.println("login " + signin.username() + " " + signin.password());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(signin.username(), signin.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            new LoginResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername()
            )
        );
    }
    


    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signup) {
        if (userRepo.existsByUsername(signup.username())) {
            return ResponseEntity
                .badRequest()
                .body(new SignupResponse("Error: username is already in use!"));
        }

        Patient patient = new Patient(
            signup.username(), 
            signup.username(), 
            null, 
            null, 
            null,
            null
        );

        User user = new User(
            signup.username(), 
            this.encoder.encode(signup.password()), 
            Role.USER,
            null
        );
        patient.setUser(user);
        user.setPatient(patient);


        userRepo.save(user);
        patientRepo.save(patient);
        return ResponseEntity.ok(
            new SignupResponse("User registered successfully!")
        );
    }
}