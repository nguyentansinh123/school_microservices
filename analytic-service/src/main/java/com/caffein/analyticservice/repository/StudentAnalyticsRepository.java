package com.caffein.analyticservice.repository;

import com.caffein.analyticservice.model.StudentAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentAnalyticsRepository extends JpaRepository<StudentAnalytics, UUID> {

    Optional<StudentAnalytics> findByStudentId(UUID studentId);

    @Query("SELECT COUNT(s) FROM StudentAnalytics s")
    Long getTotalStudents();

    @Query("SELECT AVG(s.overallAttendanceRate) FROM StudentAnalytics s")
    Double getAverageAttendanceRate();
}