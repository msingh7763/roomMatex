package com.RoomMateX.service;

import com.RoomMateX.entity.ChatMessage;
import com.RoomMateX.entity.User;
import com.RoomMateX.repository.ChatMessageRepository;
import com.RoomMateX.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository repo;
    private final UserRepository userRepo;

    // ---------- SAVE MESSAGE ----------

    @Transactional
    public ChatMessage save(Long senderId, Long receiverId, String msg){

        if(senderId.equals(receiverId))
            throw new RuntimeException("Cannot send message to yourself");

        if(msg == null || msg.trim().isEmpty())
            throw new RuntimeException("Message cannot be empty");

        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepo.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatMessage chat = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(msg)
                .seen(false)
                .timestamp(LocalDateTime.now())
                .build();

        return repo.save(chat);
    }

    // ---------- GET CONVERSATION ----------

    public List<ChatMessage> conversation(Long u1, Long u2){
        return repo.getConversation(u1, u2);
    }

    // ---------- MARK SEEN ----------

    @Transactional
    public void markSeen(Long receiverId, Long senderId){
        repo.markSeen(receiverId, senderId);
    }
}
