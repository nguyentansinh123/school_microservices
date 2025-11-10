package com.caffein.studentservice.repository;

import com.caffein.studentservice.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, UUID> {
    List<AttendanceRecord> findByStudentId(UUID studentId);

    List<AttendanceRecord> findByEnrollmentId(UUID enrollmentId);

    List<AttendanceRecord> findByCourseId(UUID courseId);

    List<AttendanceRecord> findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    Optional<AttendanceRecord> findByStudentIdAndCourseIdAndDate(UUID studentId, UUID courseId, LocalDate date);

    List<AttendanceRecord> findByStudentIdAndDateBetween(UUID studentId, LocalDate startDate, LocalDate endDate);

    List<AttendanceRecord> findByCourseIdAndDate(UUID courseId, LocalDate date);

    @Query("SELECT ar FROM AttendanceRecord ar WHERE ar.student.id = :studentId AND ar.courseId = :courseId AND ar.date BETWEEN :startDate AND :endDate")
    List<AttendanceRecord> findByStudentIdAndCourseIdAndDateRange(
            @Param("studentId") UUID studentId,
            @Param("courseId") UUID courseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(ar) FROM AttendanceRecord ar WHERE ar.student.id = :studentId AND ar.courseId = :courseId AND ar.status = :status")
    long countByStudentIdAndCourseIdAndStatus(
            @Param("studentId") UUID studentId,
            @Param("courseId") UUID courseId,
            @Param("status") AttendanceRecord.AttendanceStatus status
    );

    boolean existsByStudentIdAndCourseIdAndDate(UUID studentId, UUID courseId, LocalDate date);
}
