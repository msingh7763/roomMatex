package com.RoomMateX.controller;

import com.RoomMateX.entity.Preference;
import com.RoomMateX.entity.User;
import com.RoomMateX.service.PreferenceService;
import com.RoomMateX.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService service;
    private final UserService userService;

    @PostMapping
    public Preference save(@RequestBody Preference pref, Principal p) {
        User user = userService.getByEmail(p.getName());
        return service.save(user, pref);
    }

    @GetMapping("/me")
    public Preference get(Principal p) {
        return service.get(userService.getByEmail(p.getName()));
    }
}
