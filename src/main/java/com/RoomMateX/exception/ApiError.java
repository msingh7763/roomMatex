package com.RoomMateX.exception;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {

    private String message;
    private int status;
    private LocalDateTime timestamp;
}
