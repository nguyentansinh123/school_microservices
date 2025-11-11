package com.caffein.analyticservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentAnalyticsDTO {
    private UUID id;
    private UUID courseId;
    private String courseName;
    private String academicYear;
    private String semester;
    private Long totalEnrolled;
    private Integer totalCapacity;
    private Double enrollmentRate;
    private Long activeEnrollments;
    private Long withdrawnEnrollments;
    private Long completedEnrollments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}