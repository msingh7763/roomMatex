package com.RoomMateX.config;

import com.RoomMateX.entity.User;
import com.RoomMateX.enums.Role;
import com.RoomMateX.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args){

        if(!repo.existsByEmail("admin@roommatex.com")){

            repo.save(User.builder()
                    .name("Admin")
                    .email("admin@roommatex.com")
                    .password(encoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build());
        }
    }
}
