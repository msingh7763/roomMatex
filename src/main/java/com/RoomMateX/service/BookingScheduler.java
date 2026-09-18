package com.RoomMateX.service;

import com.RoomMateX.entity.BookingRequest;
import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.repository.BookingRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingRequestRepository repo;

    @Scheduled(fixedRate = 60000) // every minute
    public void expireBookings(){

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(15);

        List<BookingRequest> pending = repo
                .findAll()
                .stream()
                .filter(b ->
                        b.getStatus() == BookingStatus.PENDING &&
                                b.getCreated_at().isBefore(cutoff)
                )
                .toList();

        for(BookingRequest b : pending){
            b.setStatus(BookingStatus.EXPIRED);
            b.getRoom().setAvailable(true);
            repo.save(b);
        }
    }
}
