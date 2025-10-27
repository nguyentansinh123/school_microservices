package com.caffein.schoolcourseservice.repository;

import com.caffein.schoolcourseservice.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    Optional<Subject> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT s FROM Subject s JOIN s.teachers t WHERE t.id = :teacherId")
    List<Subject> findByTeacherId(UUID teacherId);
}