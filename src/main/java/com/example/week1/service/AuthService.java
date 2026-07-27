package com.example.week1.service;

import com.example.week1.dto.AuthResponse;
import com.example.week1.dto.LoginRequest;
import com.example.week1.dto.RegisterRequest;
import com.example.week1.entity.User;
import com.example.week1.repository.UserRepository;
import com.example.week1.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Bu username artıq mövcuddur!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user.getUsername());
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı: " + request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Yanlış parol!");
        }

        String jwtToken = jwtService.generateToken(user.getUsername());
        return new AuthResponse(jwtToken);
    }
}