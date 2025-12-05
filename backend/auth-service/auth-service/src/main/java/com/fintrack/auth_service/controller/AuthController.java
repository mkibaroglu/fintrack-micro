package com.fintrack.auth_service.controller;

import com.fintrack.auth_service.dto.*;
import com.fintrack.auth_service.exception.BusinessException;
import com.fintrack.auth_service.model.User;
import com.fintrack.auth_service.repository.UserRepository;
import com.fintrack.auth_service.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Auth Service";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Kullanıcı adı veya şifre hatalı");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token);
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authHeader
    ) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new TokenValidationResponse(false, null));
            }

            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (!jwtService.isTokenValid(token, username)) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new TokenValidationResponse(false, null));
            }

            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new TokenValidationResponse(false, null));
            }

            return ResponseEntity.ok(new TokenValidationResponse(true, user.getUsername()));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse(false, null));
        }
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException("Kullanıcı zaten var");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(token);
    }
}
