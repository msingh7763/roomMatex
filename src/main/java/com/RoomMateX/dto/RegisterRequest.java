package com.RoomMateX.dto;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String phone,
        String address,
        String gender,
        Integer age,
        String occupation,
        Integer budget
) {}
