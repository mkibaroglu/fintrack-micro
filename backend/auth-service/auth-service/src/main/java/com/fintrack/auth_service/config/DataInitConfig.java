package com.fintrack.auth_service.config;

import com.fintrack.auth_service.model.User;
import com.fintrack.auth_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findByUsername("mustafa")
                    .orElseGet(() -> {
                        String encoded = passwordEncoder.encode("1234");
                        User user = new User("mustafa", encoded, "USER");
                        return userRepository.save(user);
                    });
        };
    }
}