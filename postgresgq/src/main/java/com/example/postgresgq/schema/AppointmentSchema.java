package com.example.postgresgq.schema;
import java.time.LocalDateTime;
import com.example.postgresgq.model.Appointment.AppointmentType;

public class AppointmentSchema {
    public static record AppointmentUpdateResponse(
        String message
    ) {}

    public static record AppointmentUpdateRequest(
        Long appointmentId
    ) {}

    public static record AppointmentGetResponse (
        String hospitalName,
        LocalDateTime startTime,
        LocalDateTime endTime, 
        String doctorName,
        Long doctorId,
        Long appointmentId
    ) {}
}
