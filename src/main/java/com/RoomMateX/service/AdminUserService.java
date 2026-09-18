package com.RoomMateX.service;

import com.RoomMateX.dto.AdminUserDto;
import com.RoomMateX.dto.UpdateRoleRequest;
import com.RoomMateX.entity.User;
import com.RoomMateX.enums.Role;
import com.RoomMateX.exception.ResourceNotFoundException;
import com.RoomMateX.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepo;

    public Page<AdminUserDto> listUsers(String search, Pageable pageable){

        Page<User> page =
                search.isBlank()
                        ? userRepo.findAll(pageable)
                        : userRepo.findByEmailContainingIgnoreCase(search, pageable);

        return page.map(this::toDto);
    }

    private AdminUserDto toDto(User u){
        return new AdminUserDto(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getPhone(),
                u.getAddress(),
                u.getRole(),
                u.getEnabled(),
                u.getCreatedAt()
        );
    }

    @Transactional
    public void updateRole(Long id, UpdateRoleRequest req){
        User u = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        u.setRole(req.role());
    }

    @Transactional
    public void deleteUser(Long id){
        userRepo.deleteById(id);
    }
}
