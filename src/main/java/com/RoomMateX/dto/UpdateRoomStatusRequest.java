package com.RoomMateX.dto;

import com.RoomMateX.enums.RoomStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateRoomStatusRequest(

        @NotNull(message = "Status is required")
        RoomStatus status

) {}
