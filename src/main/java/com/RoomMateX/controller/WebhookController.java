package com.RoomMateX.controller;

import com.RoomMateX.entity.BookingRequest;
import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.repository.BookingRequestRepository;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    @Value("${razorpay.webhook.secret}")
    private String secret;

    private final BookingRequestRepository bookingRepo;

    @PostMapping
    public ResponseEntity<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) throws Exception {

        Utils.verifyWebhookSignature(payload, signature, secret);

        JSONObject json = new JSONObject(payload);
        Long bookingId = json.getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity")
                .getLong("notes.bookingId");

        BookingRequest br = bookingRepo.findById(bookingId).orElseThrow();
        br.setStatus(BookingStatus.PAID);
        bookingRepo.save(br);

        return ResponseEntity.ok().build();
    }
}
