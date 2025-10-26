package com.caffein.studentservice.repository;

import com.caffein.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}
