package com.caffein.analyticservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total_students")
    private Long totalStudents;

    @Column(name = "present_count")
    private Long presentCount;

    @Column(name = "absent_count")
    private Long absentCount;

    @Column(name = "late_count")
    private Long lateCount;

    @Column(name = "excused_count")
    private Long excusedCount;

    @Column(name = "attendance_rate")
    private Double attendanceRate;

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