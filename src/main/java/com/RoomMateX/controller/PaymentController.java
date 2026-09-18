package com.RoomMateX.controller;

import com.RoomMateX.entity.BookingRequest;
import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.repository.BookingRequestRepository;
import com.RoomMateX.service.EmailService;
import com.RoomMateX.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingRequestRepository bookingRepo;
    private final EmailService emailService;

    @PostMapping("/{bookingId}")
    public String createPayment(@PathVariable Long bookingId) throws Exception {

        BookingRequest br = bookingRepo.findById(bookingId).orElseThrow();

        var order = paymentService.createOrder(br.getRoom().getRent());

        return order.toString();
    }

    @PutMapping("/{bookingId}/success")
    public void paymentSuccess(@PathVariable Long bookingId){

        BookingRequest br = bookingRepo.findById(bookingId).orElseThrow();
        br.setStatus(BookingStatus.PAID);

        bookingRepo.save(br);

        emailService.sendHtml(
                br.getRequester().getEmail(),
                "RoomMateX Booking Confirmed",
                """
                <h2>🎉 Booking Confirmed!</h2>
                <p>Your booking for <b>%s</b> has been successfully completed.</p>
                <p>Rent Paid: ₹%d</p>
                <p>Thank you for using RoomMateX ❤️</p>
                """.formatted(
                        br.getRoom().getTitle(),
                        br.getRoom().getRent()
                )
        );

    }

}
