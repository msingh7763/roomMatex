package com.RoomMateX.service;

import com.RoomMateX.dto.AdminDashboardSummaryDto;
import com.RoomMateX.dto.CountResponse;
import com.RoomMateX.repository.BookingRequestRepository;
import com.RoomMateX.repository.RoomRepository;
import com.RoomMateX.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepo;
    private final RoomRepository roomRepo;
    private final BookingRequestRepository bookingRepo;

    public AdminDashboardSummaryDto getSummary(){

        long totalUsers = userRepo.count();
        long totalRooms = roomRepo.count();
        long activeRooms = roomRepo.countByAvailableTrue(); // adjust if needed
        long bookings = bookingRepo.count();

        return new AdminDashboardSummaryDto(
                totalUsers,
                totalRooms,
                activeRooms,
                bookings,
                0, // growth later
                0,
                0,
                0
        );
    }
    public List<CountResponse> roomsByCity() {
        return roomRepo.countRoomsByCity()
                .stream()
                .map(r -> new CountResponse((String) r[0], (Long) r[1]))
                .toList();
    }

    public List<CountResponse> usersByRole() {
        return userRepo.countUsersByRole()
                .stream()
                .map(r -> new CountResponse(r[0].toString(), (Long) r[1]))
                .toList();
    }
}
