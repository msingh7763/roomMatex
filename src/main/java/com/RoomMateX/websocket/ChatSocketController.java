package com.RoomMateX.websocket;

import com.RoomMateX.dto.ChatPayload;
import com.RoomMateX.entity.ChatMessage;
import com.RoomMateX.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/socket/chat")
    public void send(ChatPayload payload){

        ChatMessage saved = chatService.save(
                payload.senderId(),
                payload.receiverId(),
                payload.message()
        );

        template.convertAndSendToUser(
                payload.receiverEmail(),
                "/queue/messages",
                saved
        );
    }
}
