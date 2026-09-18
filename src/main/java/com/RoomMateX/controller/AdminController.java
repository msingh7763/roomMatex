package com.RoomMateX.controller;

import com.RoomMateX.dto.AdminDashboardSummaryDto;
import com.RoomMateX.dto.AdminUserDto;
import com.RoomMateX.dto.UpdateRoleRequest;
import com.RoomMateX.service.AdminDashboardService;
import com.RoomMateX.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUserService adminUserService;
    private final AdminDashboardService dashboardService;

    // ================= USERS =================

    @GetMapping("/users")
    public Page<AdminUserDto> users(
            @RequestParam(defaultValue = "") String search,
            Pageable pageable
    ){
        return adminUserService.listUsers(search, pageable);
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest req
    ){
        adminUserService.updateRole(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
