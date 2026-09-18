package com.RoomMateX.repository;

import com.RoomMateX.entity.ChatMessage;
import com.RoomMateX.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("""
      SELECT c FROM ChatMessage c
      WHERE (c.sender.id = :u1 AND c.receiver.id = :u2)
         OR (c.sender.id = :u2 AND c.receiver.id = :u1)
      ORDER BY c.timestamp
    """)
    List<ChatMessage> getConversation(Long u1, Long u2);

    @Modifying
    @Transactional
    @Query("""
     UPDATE ChatMessage c SET c.seen = true
     WHERE c.receiver.id = :receiverId
       AND c.sender.id = :senderId
    """)
    void markSeen(Long receiverId, Long senderId);
}
