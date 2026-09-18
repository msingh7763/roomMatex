package com.RoomMateX.auth;

import com.RoomMateX.dto.AuthResponse;
import com.RoomMateX.dto.LoginRequest;
import com.RoomMateX.dto.RegisterRequest;
import com.RoomMateX.entity.RefreshToken;
import com.RoomMateX.entity.User;
import com.RoomMateX.entity.VerificationToken;
import com.RoomMateX.enums.Role;
import com.RoomMateX.repository.RefreshTokenRepository;
import com.RoomMateX.repository.UserRepository;
import com.RoomMateX.repository.VerificationTokenRepository;
import com.RoomMateX.security.JwtUtil;
import com.RoomMateX.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final VerificationTokenRepository verifyRepo;
    private final EmailService emailService;

    // ================= REGISTER =================

    public String register(RegisterRequest r){

        if(userRepo.existsByEmail(r.email())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        User u = User.builder()
                .name(r.name())
                .email(r.email())
                .password(encoder.encode(r.password()))
                .phone(r.phone())
                .address(r.address())
                .gender(r.gender())
                .age(r.age())
                .occupation(r.occupation())
                .budget(r.budget())
                .role(Role.USER)
                .enabled(false) // ✅ WAIT FOR EMAIL VERIFICATION
                .build();

        userRepo.save(u);

        // create verification token
        String token = UUID.randomUUID().toString();

        verifyRepo.save(
                VerificationToken.builder()
                        .token(token)
                        .user(u)
                        .expiry(LocalDateTime.now().plusHours(24))
                        .build()
        );

        // send email
        emailService.sendHtml(
                u.getEmail(),
                "Verify your RoomMateX account",
                """
                <h2>Welcome to RoomMateX 👋</h2>
                <p>Please verify your email:</p>

                <a href="http://localhost:8081/api/auth/verify?token=%s"
                   style="padding:10px 16px;background:#22c55e;color:white;border-radius:6px;text-decoration:none">
                   Verify Email
                </a>
                """.formatted(token)
        );

        return "Registration successful. Please verify your email.";
    }

    // ================= LOGIN =================

    public AuthResponse login(LoginRequest r){

        User u = userRepo.findByEmail(r.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Account not found"
                        )
                );

        if(Boolean.FALSE.equals(u.getEnabled())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Please verify your email first"
            );
        }

        if(!encoder.matches(r.getPassword(), u.getPassword())){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String accessToken = jwt.generateToken(u.getEmail());

        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = refreshRepo.findByUser(u)
                .orElse(
                        RefreshToken.builder()
                                .user(u)
                                .build()
                );

        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiry(LocalDateTime.now().plusDays(7));

        refreshRepo.save(refreshToken);

        return new AuthResponse(accessToken, refreshTokenValue, u);
    }

    // ================= LOGOUT =================

    public void logout(User user){
        refreshRepo.deleteByUser(user);
    }
}
