package com.RoomMateX.controller;

import com.RoomMateX.dto.AdminRoomDto;
import com.RoomMateX.dto.UpdateRoomStatusRequest;
import com.RoomMateX.enums.RoomStatus;
import com.RoomMateX.service.AdminRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/rooms")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    private final AdminRoomService adminRoomService;

    // GET /api/admin/rooms?page=0&size=10&city=&status=
    @GetMapping
    public Page<AdminRoomDto> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String status,
            Pageable pageable
    ){
        return adminRoomService.listRooms(city, status, pageable);
    }

    // PATCH /api/admin/rooms/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomStatusRequest req
    ){
        adminRoomService.updateStatus(id, req.status());
        return ResponseEntity.ok().build();
    }

    // DELETE /api/admin/rooms/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        adminRoomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
