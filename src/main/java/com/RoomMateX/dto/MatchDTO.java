package com.RoomMateX.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchDTO {

    private Long id;
    private Long userId;
    private String name;
    private Integer age;
    private String city;
    private Integer budget;
    private Integer score;
}
