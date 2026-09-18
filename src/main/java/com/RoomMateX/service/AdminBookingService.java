package com.RoomMateX.service;

import com.RoomMateX.dto.AdminBookingDto;
import com.RoomMateX.entity.BookingRequest;
import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.repository.BookingRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AdminBookingService {

    private final BookingRequestRepository bookingRepo;

    public Page<AdminBookingDto> listBookings(String status, Pageable pageable){

        BookingStatus bookingStatus =
                status == null || status.isBlank()
                        ? null
                        : BookingStatus.valueOf(status.toUpperCase());

        Page<BookingRequest> bookings =
                bookingStatus == null
                        ? bookingRepo.findAll(pageable)
                        : bookingRepo.findByStatus(bookingStatus, pageable);

        return bookings.map(this::toDto);
    }

    private AdminBookingDto toDto(BookingRequest b){
        return new AdminBookingDto(
                b.getId(),
                b.getRequester().getEmail(),
                b.getRoom().getTitle(),
                b.getStatus(),
                b.getCreated_at()
        );
    }

    @Transactional
    public void updateStatus(Long id, BookingStatus status){
        BookingRequest b = bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        b.setStatus(status);
    }
}
