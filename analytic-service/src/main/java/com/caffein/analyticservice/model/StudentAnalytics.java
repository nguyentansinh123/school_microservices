package com.caffein.analyticservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "student_id", nullable = false, unique = true)
    private UUID studentId;

    @Column(name = "registration_id")
    private String registrationId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "total_enrollments")
    private Long totalEnrollments;

    @Column(name = "active_enrollments")
    private Long activeEnrollments;

    @Column(name = "completed_courses")
    private Long completedCourses;

    @Column(name = "withdrawn_courses")
    private Long withdrawnCourses;

    @Column(name = "overall_attendance_rate")
    private Double overallAttendanceRate;

    @Column(name = "total_absences")
    private Long totalAbsences;

    @Column(name = "total_classes_attended")
    private Long totalClassesAttended;

    @Column(name = "total_classes")
    private Long totalClasses;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}