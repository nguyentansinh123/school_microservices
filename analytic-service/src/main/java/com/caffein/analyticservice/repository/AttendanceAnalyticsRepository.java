package com.caffein.analyticservice.repository;

import com.caffein.analyticservice.model.AttendanceAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceAnalyticsRepository extends JpaRepository<AttendanceAnalytics, UUID> {

    Optional<AttendanceAnalytics> findByCourseIdAndDate(UUID courseId, LocalDate date);

    List<AttendanceAnalytics> findByCourseId(UUID courseId);

    List<AttendanceAnalytics> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT AVG(a.attendanceRate) FROM AttendanceAnalytics a WHERE a.date BETWEEN :startDate AND :endDate")
    Double getAverageAttendanceRate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(a.absentCount) FROM AttendanceAnalytics a WHERE a.date = :date")
    Long getTotalAbsencesByDate(@Param("date") LocalDate date);
}