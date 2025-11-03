package com.caffein.studentservice.repository;

import com.caffein.studentservice.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentId(@Param("studentId") UUID studentId);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.courseId = :courseId")
    Optional<Enrollment> findByStudentIdAndCourseId(@Param("studentId") UUID studentId, @Param("courseId") UUID courseId);

    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);

    long countByCourseId(UUID courseId);
}