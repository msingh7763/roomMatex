package com.RoomMateX.dto;

import com.RoomMateX.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(
        @NotNull Role role
){}
