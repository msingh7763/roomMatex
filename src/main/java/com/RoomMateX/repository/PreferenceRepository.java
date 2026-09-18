package com.RoomMateX.repository;

import com.RoomMateX.entity.Preference;
import com.RoomMateX.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    Optional<Preference> findByUser(User user);
}
