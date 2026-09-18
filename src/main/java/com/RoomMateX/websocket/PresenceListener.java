package com.RoomMateX.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class PresenceListener {

    private final SimpMessagingTemplate template;

    @EventListener
    public void connect(SessionConnectEvent e){
        String email = e.getUser().getName();
        template.convertAndSend("/topic/online", email);
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent e){
        String email = e.getUser().getName();
        template.convertAndSend("/topic/offline", email);
    }
}
