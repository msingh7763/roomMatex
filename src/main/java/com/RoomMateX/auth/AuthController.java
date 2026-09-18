package com.RoomMateX.auth;

import com.RoomMateX.dto.AuthResponse;
import com.RoomMateX.dto.LoginRequest;
import com.RoomMateX.dto.RegisterRequest;
import com.RoomMateX.entity.PasswordReset;
import com.RoomMateX.entity.RefreshToken;
import com.RoomMateX.entity.User;
import com.RoomMateX.entity.VerificationToken;
import com.RoomMateX.repository.PasswordResetRepository;
import com.RoomMateX.repository.RefreshTokenRepository;
import com.RoomMateX.repository.UserRepository;
import com.RoomMateX.repository.VerificationTokenRepository;
import com.RoomMateX.security.JwtUtil;
import com.RoomMateX.service.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService service;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordResetRepository resetRepo;
    private final UserRepository userRepo;
    private final JwtUtil jwt;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final VerificationTokenRepository verifyRepo;


    // ---------------- REGISTER ----------------

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req){
        try {
            return ResponseEntity.ok(service.register(req));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    // ---------------- LOGIN ----------------

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r){
        try {
            return ResponseEntity.ok(service.login(r));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    // ---------------- REFRESH TOKEN ----------------

    @PostMapping("/refresh")
    public String refresh(@RequestParam String token){

        RefreshToken rt = refreshRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(rt.getExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Refresh token expired");

        return jwt.generateToken(rt.getUser().getEmail());
    }

    // ---------------- FORGOT PASSWORD ----------------

    @PostMapping("/forgot")
    public void forgot(@RequestParam String email){

        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        resetRepo.save(
                PasswordReset.builder()
                        .token(token)
                        .user(u)
                        .expiry(LocalDateTime.now().plusMinutes(15))
                        .build()
        );

        emailService.sendHtml(
                email,
                "RoomMateX Password Reset",
                """
                <h2>Reset your password</h2>
                <p>Click below:</p>
                <a href="http://localhost:5173/reset/%s"
                   style="padding:10px 16px;background:#f97316;color:white;border-radius:6px;text-decoration:none">
                   Reset Password
                </a>
                """.formatted(token)
        );



    }

    // ---------------- RESET PASSWORD ----------------

    @PostMapping("/reset")
    public void reset(@RequestParam String token,
                      @RequestParam String password){

        PasswordReset pr = resetRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if(pr.getExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Reset token expired");

        User u = pr.getUser();
        u.setPassword(encoder.encode(password));

        userRepo.save(u);
    }
    @GetMapping("/verify")
    public void verify(@RequestParam String token,
                       HttpServletResponse response) throws IOException {

        VerificationToken v = verifyRepo.findByToken(token)
                .orElseThrow();

        User u = v.getUser();
        u.setEnabled(true);
        userRepo.save(u);

        response.sendRedirect("http://localhost:5173/login");
    }

    // ---------------- LOGOUT ----------------

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String header) {

        String token = header.substring(7);
        String email = jwt.extractEmail(token);

        User user = userRepo.findByEmail(email)
                .orElseThrow();

        refreshRepo.deleteByUser(user);

        return ResponseEntity.ok("Logged out successfully");
    }


}
