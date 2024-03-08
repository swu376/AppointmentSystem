package com.example.postgresgq.schema;

public class AuthSchema {
    public static record SignupRequest (
        String username,
        String password
    ) {} 

    public static record SignupResponse (
        String message
    ) {}

    public static record LoginRequest (
        String username,
        String password
    ) {}

    public static record LoginResponse(
        String token,
        long id,
        String username
    ) {}
}
