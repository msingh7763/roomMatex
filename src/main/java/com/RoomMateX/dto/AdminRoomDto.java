package com.RoomMateX.dto;

import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.enums.RoomStatus;

import java.time.LocalDateTime;

public record AdminRoomDto(
        Long id,
        String title,
        String city,
        Double price,
        RoomStatus status,
        String ownerEmail,
        LocalDateTime createdAt
) {}
