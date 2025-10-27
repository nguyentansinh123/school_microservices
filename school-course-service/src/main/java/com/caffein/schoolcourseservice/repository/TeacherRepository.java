package com.caffein.schoolcourseservice.repository;

import com.caffein.schoolcourseservice.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    Optional<Teacher> findByUserId(UUID userId);
    Optional<Teacher> findByEmail(String email);
    boolean existsByUserId(UUID userId);
    boolean existsByEmail(String email);

    @Query("SELECT t FROM Teacher t JOIN t.subjects s WHERE s.id = :subjectId")
    List<Teacher> findBySubjectId(UUID subjectId);
}