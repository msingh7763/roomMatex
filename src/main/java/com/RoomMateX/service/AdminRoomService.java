package com.RoomMateX.service;

import com.RoomMateX.dto.AdminRoomDto;
import com.RoomMateX.entity.Room;
import com.RoomMateX.enums.BookingStatus;
import com.RoomMateX.enums.RoomStatus;
import com.RoomMateX.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AdminRoomService {

    private final RoomRepository roomRepo;

    public Page<AdminRoomDto> listRooms(String city, String status, Pageable pageable){

        Page<Room> rooms = roomRepo.findFiltered(city, status, pageable);

        return rooms.map(this::toDto);
    }

    private AdminRoomDto toDto(Room r){
        return new AdminRoomDto(
                r.getId(),
                r.getTitle(),
                r.getCity(),
                r.getPrice(),
                r.getStatus(),
                r.getOwner().getEmail(),
                r.getCreated_at()
        );
    }

    @Transactional
    public void updateStatus(Long id, RoomStatus status){
        Room r = roomRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        r.setStatus(status);
    }

    @Transactional
    public void deleteRoom(Long id){
        roomRepo.deleteById(id);
    }
}
