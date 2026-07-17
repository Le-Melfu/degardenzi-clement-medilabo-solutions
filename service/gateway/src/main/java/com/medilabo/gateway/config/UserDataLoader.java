package com.medilabo.gateway.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.medilabo.gateway.model.User;
import com.medilabo.gateway.repository.UserRepository;

@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Médecin (utilisateur métier)
        createIfAbsent("doctor", "password");
        // Compte de service (appels inter-microservices, ex. risk-assessment)
        createIfAbsent("service", "service");
    }

    private void createIfAbsent(String username, String rawPassword) {
        if (!userRepository.existsByUsername(username)) {
            userRepository.save(new User(username, passwordEncoder.encode(rawPassword)));
        }
    }
}
