package com.caffein.analyticservice.repository;

import com.caffein.analyticservice.model.EnrollmentAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentAnalyticsRepository extends JpaRepository<EnrollmentAnalytics, UUID> {

    Optional<EnrollmentAnalytics> findByCourseId(UUID courseId);

    List<EnrollmentAnalytics> findByAcademicYearAndSemester(String academicYear, String semester);

    @Query("SELECT AVG(e.enrollmentRate) FROM EnrollmentAnalytics e WHERE e.academicYear = :academicYear AND e.semester = :semester")
    Double getAverageEnrollmentRate(@Param("academicYear") String academicYear, @Param("semester") String semester);

    @Query("SELECT SUM(e.totalEnrolled) FROM EnrollmentAnalytics e")
    Long getTotalEnrollments();
}