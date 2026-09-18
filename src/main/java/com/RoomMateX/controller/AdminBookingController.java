package com.RoomMateX.controller;

import com.RoomMateX.dto.AdminBookingDto;
import com.RoomMateX.dto.UpdateBookingStatusRequest;
import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.service.AdminBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    // GET /api/admin/bookings?page=0&size=10&status=
    @GetMapping
    public Page<AdminBookingDto> list(
            @RequestParam(required = false) String status,
            Pageable pageable
    ){
        return adminBookingService.listBookings(status, pageable);
    }

    // PATCH /api/admin/bookings/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingStatusRequest req
    ){
        adminBookingService.updateStatus(id, req.status());
        return ResponseEntity.ok().build();
    }
}
