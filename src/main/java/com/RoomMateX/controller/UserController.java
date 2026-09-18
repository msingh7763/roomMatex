package com.RoomMateX.controller;

import com.RoomMateX.entity.User;
import com.RoomMateX.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/me")
    public User me(Principal p) {
        return service.getByEmail(p.getName());
    }

    @PutMapping("/me")
    public User update(@RequestBody User user, Principal p) {
        User existing = service.getByEmail(p.getName());
        user.setId(existing.getId());
        return service.update(user);
    }
}
