package com.RoomMateX.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealTimeNotificationService {

    private final SimpMessagingTemplate template;

    public void send(String email, String message){

        template.convertAndSendToUser(
                email,
                "/queue/notifications",
                message
        );
    }
}
