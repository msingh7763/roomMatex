package com.RoomMateX.repository;

import com.RoomMateX.entity.BookingRequest;
import com.RoomMateX.entity.User;
import com.RoomMateX.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    List<BookingRequest> findByRequester(User user);

    List<BookingRequest> findByRoomOwnerId(Long ownerId);

    boolean existsByRoomIdAndRequesterId(Long roomId, Long requesterId);

    @Query("SELECT COUNT(b) FROM BookingRequest b")
    long countBookings();

    long count();

    Page<BookingRequest> findByStatus(BookingStatus status, Pageable pageable);



}
