package com.example.postgresgq.repo;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.postgresgq.model.Appointment;
import com.example.postgresgq.model.Doctor;
import com.example.postgresgq.model.Patient;
import com.example.postgresgq.model.Appointment.AppointmentStatus;

@RepositoryRestResource
public interface AppointmentRepo extends JpaRepository<Appointment, Long>{
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :date AND a.hospital.name = :hospitalName")
    List<Appointment> findByAppointmentDateAndHospitalName(@Param("date") LocalDateTime date, @Param("hospitalName") String hospitalName);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :sTime AND a.appointmentDate <= :eTime AND a.hospital.name = :hospitalName AND a.appointmentStatus = :appointmentStatus")
    List<Appointment> findByAppointmentDateAndHospitalNameAndAppointmentStatus(
        @Param("sTime") LocalDateTime sTime, 
        @Param("eTime") LocalDateTime eTime,
        @Param("hospitalName") String hospitalName,
        @Param("appointmentStatus") AppointmentStatus appointmentStatus
    );

    List<Appointment> findByPatient(Patient patient);

    boolean existsByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByAppointmentDateBetweenAndDoctor(LocalDateTime startDateTime, LocalDateTime endDateTime, Doctor doctor);
}
