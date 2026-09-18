package com.RoomMateX.dto;

import com.RoomMateX.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {

    private Long id;
    private Long roomId;
    private String roomTitle;
    private String city;
    private String requesterName;
    private BookingStatus status;
}
