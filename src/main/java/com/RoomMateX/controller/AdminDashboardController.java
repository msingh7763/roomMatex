package com.RoomMateX.controller;

import com.RoomMateX.dto.AdminDashboardSummaryDto;
import com.RoomMateX.dto.CountResponse;
import com.RoomMateX.dto.DashboardSummaryResponse;
import com.RoomMateX.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService service;

    @GetMapping("/summary")
    public AdminDashboardSummaryDto summary() {
        return service.getSummary();
    }

    @GetMapping("/rooms-by-city")
    public List<CountResponse> roomsByCity() {
        return service.roomsByCity();
    }

    @GetMapping("/users-by-role")
    public List<CountResponse> usersByRole() {
        return service.usersByRole();
    }
}
