package com.RoomMateX.dto;

import com.RoomMateX.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateBookingStatusRequest(
        @NotNull BookingStatus status
) {}
