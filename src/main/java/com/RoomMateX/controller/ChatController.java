package com.RoomMateX.controller;

import com.RoomMateX.dto.ChatPayload;
import com.RoomMateX.dto.TypingPayload;
import com.RoomMateX.entity.ChatMessage;
import com.RoomMateX.entity.User;
import com.RoomMateX.service.ChatService;
import com.RoomMateX.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;
    private final SimpMessagingTemplate template;
    private final UserService userService;

    // ---------- CHAT HISTORY ----------

    @GetMapping("/{otherId}")
    public List<ChatMessage> history(@PathVariable Long otherId,
                                     Authentication auth){

        User me = userService.getByEmail(auth.getName());

        return service.conversation(me.getId(), otherId);
    }

    // ---------- SEND MESSAGE (WEBSOCKET) ----------

    @MessageMapping("/chat")
    public void send(ChatPayload payload){

        ChatMessage saved =
                service.save(payload.senderId(),
                        payload.receiverId(),
                        payload.message());

        template.convertAndSendToUser(
                payload.receiverEmail(),
                "/queue/messages",
                saved
        );
    }

    // ---------- MARK SEEN ----------

    @PutMapping("/{otherId}/seen")
    public void seen(@PathVariable Long otherId, Authentication auth){

        User me = userService.getByEmail(auth.getName());
        service.markSeen(me.getId(), otherId);
    }

    // ---------- TYPING ----------

    @MessageMapping("/typing")
    public void typing(TypingPayload p){

        template.convertAndSendToUser(
                p.receiverEmail(),
                "/queue/typing",
                p.senderName()
        );
    }

    // ---------- FILE UPLOAD ----------

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception{

        Path uploadDir = Paths.get("uploads");

        if(!Files.exists(uploadDir))
            Files.createDirectories(uploadDir);

        Path path = uploadDir.resolve(file.getOriginalFilename());

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "uploads/" + file.getOriginalFilename();
    }
}
