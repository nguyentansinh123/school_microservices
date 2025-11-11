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
@Table(name = "enrollment_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "semester")
    private String semester;

    @Column(name = "total_enrolled")
    private Long totalEnrolled;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    @Column(name = "enrollment_rate")
    private Double enrollmentRate;

    @Column(name = "active_enrollments")
    private Long activeEnrollments;

    @Column(name = "withdrawn_enrollments")
    private Long withdrawnEnrollments;

    @Column(name = "completed_enrollments")
    private Long completedEnrollments;

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