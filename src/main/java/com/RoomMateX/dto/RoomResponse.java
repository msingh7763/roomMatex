package com.RoomMateX.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponse {

    private Long id;
    private String title;
    private String description;
    private Integer rent;
    private String city;
    private Double latitude;
    private Double longitude;
    private Boolean available;
    private String ownerName;
}
