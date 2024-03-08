package com.example.postgresgq.schema;

public class DoctorSchema {
    public static record DoctorAddResponse(
        String name,
        String hospitalName
    ) {}   

    public static record DoctorAddRequest(
        String name,
        String email,
        String hospitalName
    ) {}
}
