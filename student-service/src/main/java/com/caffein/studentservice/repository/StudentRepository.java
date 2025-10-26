package com.caffein.studentservice.repository;

import com.caffein.studentservice.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Optional<Student> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.guardians WHERE s.id = :id")
    Optional<Student> findByIdWithGuardians(@Param("id") UUID id);

    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.registrationId) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Student> findBySearchCriteria(@Param("search") String search, Pageable pageable);
}
