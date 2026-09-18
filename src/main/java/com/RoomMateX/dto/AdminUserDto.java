package com.RoomMateX.dto;

import com.RoomMateX.enums.Role;

import java.time.LocalDateTime;

public record AdminUserDto(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        Role role,
        Boolean enabled,
        LocalDateTime createdAt
) {}
