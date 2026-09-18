package com.RoomMateX.dto;

import com.RoomMateX.enums.BookingStatus;
import java.time.LocalDateTime;

public record AdminBookingDto(
        Long id,
        String userEmail,
        String roomTitle,
        BookingStatus status,
        LocalDateTime createdAt
) {}
