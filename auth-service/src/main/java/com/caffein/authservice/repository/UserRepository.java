package com.caffein.authservice.repository;

import java.util.Optional;

import com.caffein.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);
}
