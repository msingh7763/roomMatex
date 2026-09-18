package com.RoomMateX.service;

import com.RoomMateX.entity.Notification;
import com.RoomMateX.entity.User;
import com.RoomMateX.repository.NotificationRepository;
import com.RoomMateX.websocket.RealTimeNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final RealTimeNotificationService ws;

    // Create + push notification
    public void notify(User user, String msg){

        repo.save(Notification.builder()
                .user(user)
                .message(msg)
                .seen(false)
                .build());

        ws.send(user.getEmail(), msg);
    }

    // Get all notifications
    public List<Notification> my(User user){
        return repo.findByUserOrderByCreatedAtDesc(user);
    }

    // Mark as seen
    public void markSeen(Long id){
        Notification n = repo.findById(id).orElseThrow();
        n.setSeen(true);
        repo.save(n);
    }

    // Count unread
    public Long unreadCount(User user){
        return repo.countByUserAndSeenFalse(user);
    }
}
