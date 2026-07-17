package com.medilabo.gateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medilabo.gateway.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
