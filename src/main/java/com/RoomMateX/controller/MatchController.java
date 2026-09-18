package com.RoomMateX.controller;

import com.RoomMateX.dto.MatchDTO;
import com.RoomMateX.entity.Match;
import com.RoomMateX.entity.User;
import com.RoomMateX.service.MatchService;
import com.RoomMateX.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService service;
    private final UserService userService;

    @GetMapping
    public List<MatchDTO> myMatches(Authentication auth){

        User me = userService.getByEmail(auth.getName());
        return service.myMatches(me);
    }
    @GetMapping("/top")
    public List<MatchDTO> top(Authentication auth,
                              @RequestParam(defaultValue="5") int n){

        User me = userService.getByEmail(auth.getName());
        return service.topMatches(me,n);
    }

}
