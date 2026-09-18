package com.RoomMateX.service;

import com.RoomMateX.entity.Preference;
import com.RoomMateX.entity.User;
import com.RoomMateX.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository repo;

    public Preference save(User user, Preference pref) {
        pref.setUser(user);
        return repo.save(pref);
    }

    public Preference get(User user) {
        return repo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Preference not found"));
    }
}
