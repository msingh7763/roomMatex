package com.RoomMateX.dto;

public record ChatPayload(
        Long senderId,
        Long receiverId,
        String receiverEmail,
        String message
) {}