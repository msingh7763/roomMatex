package com.RoomMateX.service;

import com.RoomMateX.dto.RoomResponse;
import com.RoomMateX.entity.Room;
import com.RoomMateX.entity.User;
import com.RoomMateX.repository.RoomRepository;

import com.RoomMateX.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository repo;

    @Cacheable("rooms")
    public Page<RoomResponse> getAll(int page, int size) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("created_at").descending());

        return repo.findByAvailableTrue(pageable)
                .map(this::mapToDto);
    }

    public Page<RoomResponse> search(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());

        return repo
                .findByTitleContainingIgnoreCaseAndAvailableTrue(keyword, pageable)
                .map(this::mapToDto);
    }

    @Cacheable(value="room", key="#id")
    public RoomResponse getById(Long id) {

        Room room = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return mapToDto(room);
    }

    @CacheEvict(value={"rooms","room"}, allEntries=true)
    public RoomResponse create(Room room, User owner) {

        room.setOwner(owner);
        room.setAvailable(true);

        return mapToDto(repo.save(room));
    }

    @CacheEvict(value={"rooms","room"}, allEntries=true)
    public void delete(Long id){

        if(!repo.existsById(id))
            throw new RuntimeException("Room not found");

        repo.deleteById(id);
    }

    private RoomResponse mapToDto(Room room) {

        return RoomResponse.builder()
                .id(room.getId())
                .title(room.getTitle())
                .description(room.getDescription())
                .rent(room.getRent())
                .city(room.getCity())
                .latitude(room.getLatitude())
                .longitude(room.getLongitude())
                .available(room.getAvailable())
                .ownerName(room.getOwner().getName())
                .build();
    }
}
