package com.RoomMateX.controller;

import com.RoomMateX.dto.BookingResponse;
import com.RoomMateX.entity.User;
import com.RoomMateX.service.BookingService;
import com.RoomMateX.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@CrossOrigin
public class BookingController {

    private final BookingService service;
    private final UserService userService;

    // Send booking request
    @PostMapping("/{roomId}")
    public BookingResponse request(@PathVariable Long roomId, Authentication auth){

        User user = userService.getByEmail(auth.getName());
        return service.request(roomId, user);
    }

    // My bookings
    @GetMapping("/my")
    public List<BookingResponse> my(Authentication auth){

        User user = userService.getByEmail(auth.getName());
        return service.myBookings(user);
    }

    // Owner bookings
    @GetMapping("/owner")
    public List<BookingResponse> owner(Authentication auth){

        User user = userService.getByEmail(auth.getName());
        return service.ownerBookings(user);
    }

    // Approve
    @PutMapping("/{id}/approve")
    public BookingResponse approve(@PathVariable Long id){
        return service.approve(id);
    }

    // Reject
    @PutMapping("/{id}/reject")
    public BookingResponse reject(@PathVariable Long id){
        return service.reject(id);
    }
    @PutMapping("/{id}/cancel")
    public BookingResponse cancel(
            @PathVariable Long id,
            @RequestParam String paymentId
    ) throws Exception {

        return service.cancel(id, paymentId);
    }

}
