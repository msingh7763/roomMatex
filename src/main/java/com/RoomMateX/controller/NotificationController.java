package com.RoomMateX.controller;

import com.RoomMateX.entity.Notification;
import com.RoomMateX.entity.User;
import com.RoomMateX.service.NotificationService;
import com.RoomMateX.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final UserService userService;

    // Get all notifications
    @GetMapping
    public List<Notification> my(Authentication auth){
        User user = userService.getByEmail(auth.getName());
        return service.my(user);
    }

    // Mark notification as seen
    @PutMapping("/{id}/seen")
    public void seen(@PathVariable Long id){
        service.markSeen(id);
    }

    // Get unread count
    @GetMapping("/unread-count")
    public Long unread(Authentication auth){
        User user = userService.getByEmail(auth.getName());
        return service.unreadCount(user);
    }
}
