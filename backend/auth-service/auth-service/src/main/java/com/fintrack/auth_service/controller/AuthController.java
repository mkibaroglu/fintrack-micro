package com.fintrack.auth_service.controller;

import com.fintrack.auth_service.dto.LoginRequest;
import com.fintrack.auth_service.dto.LoginResponse;
import com.fintrack.auth_service.exception.BusinessException;
import com.fintrack.auth_service.model.User;
import com.fintrack.auth_service.service.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService; // şimdilik manuel, sonra @Bean yaparız

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Demo user
    private final User demoUser = new User("mustafa", "1234", "USER");

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Auth Service";
    }

    @GetMapping("/error")
    public String error() {
        // Test amaçlı bilerek hata atıyoruz
        throw new BusinessException("Örnek business hatası: Bakiye yetersiz.");
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // Çok basit kontrol – gerçek projede DB + password encoder
        if (demoUser.getUsername().equals(request.getUsername())
                && demoUser.getPassword().equals(request.getPassword())) {

            String token = jwtService.generateToken(demoUser.getUsername());
            return new LoginResponse(token);
        }

        // Burada global exception handler devreye girer (örneğin CustomException fırlatabiliriz)
        throw new RuntimeException("Kullanıcı adı veya şifre hatalı");
    }
}
