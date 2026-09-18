package com.RoomMateX.repository;

import com.RoomMateX.entity.Room;
import com.RoomMateX.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findByAvailableTrue(Pageable pageable);

    Page<Room> findByCityContainingIgnoreCaseAndAvailableTrue(String city, Pageable pageable);

    Page<Room> findByTitleContainingIgnoreCaseAndAvailableTrue(String title, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Room r")
    long countAllRooms();

    @Query("SELECT COUNT(r) FROM Room r WHERE r.status='ACTIVE'")
    long countActiveRooms();

    @Query("SELECT r.city, COUNT(r) FROM Room r GROUP BY r.city")
    List<Object[]> countRoomsByCity();

    @Query("""
SELECT r FROM Room r
WHERE (:city IS NULL OR r.city = :city)
AND (:status IS NULL OR r.status = :status)
""")
    Page<Room> findFiltered(
            @Param("city") String city,
            @Param("status") String status,
            Pageable pageable
    );

    long countByStatus(RoomStatus status);

    long countByAvailableTrue();

}
