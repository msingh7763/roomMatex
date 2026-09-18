package com.RoomMateX.service;

import com.RoomMateX.entity.User;
import com.RoomMateX.exception.ResourceNotFoundException;
import com.RoomMateX.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    public User getByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User update(User user) {
        return repo.save(user);
    }

}
