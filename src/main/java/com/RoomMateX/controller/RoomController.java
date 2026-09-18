package com.RoomMateX.controller;

import com.RoomMateX.dto.RoomResponse;
import com.RoomMateX.entity.Room;
import com.RoomMateX.entity.User;
import com.RoomMateX.service.RoomService;
import com.RoomMateX.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin
public class RoomController {

    private final RoomService service;
    private final UserService userService;

    // Create Room
    @PostMapping
    public RoomResponse create(@RequestBody Room room,
                               Authentication auth) {

        User user = userService.getByEmail(auth.getName());
        return service.create(room, user);
    }

    // List rooms
    @GetMapping
    public Page<RoomResponse> getAll(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="6") int size
    ){
        return service.getAll(page,size);
    }

    // Search
    @GetMapping("/search")
    public Page<RoomResponse> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="6") int size
    ){
        return service.search(keyword,page,size);
    }

    // Single room
    @GetMapping("/{id}")
    public RoomResponse get(@PathVariable Long id){
        return service.getById(id);
    }

    // DELETE — ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
