package com.RoomMateX.dto;


public record AdminDashboardSummaryDto(
        long totalUsers,
        long totalRooms,
        long activeRooms,
        long bookings,
        int usersGrowthPct,
        int roomsGrowthPct,
        int activeRoomsGrowthPct,
        int bookingsGrowthPct
) {}