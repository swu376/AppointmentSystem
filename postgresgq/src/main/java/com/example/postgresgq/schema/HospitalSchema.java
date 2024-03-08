package com.example.postgresgq.schema;

public class HospitalSchema {
    public static record HospitalResponse(
        String name,
        String location
    ) {}

    public static record HospitalAddRequest(
        String name,
        String location,
        String openTime,
        String closeTime
    ) {}
}
