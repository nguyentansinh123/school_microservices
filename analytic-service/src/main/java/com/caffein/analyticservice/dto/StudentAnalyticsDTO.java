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
public class StudentAnalyticsDTO {
    private UUID id;
    private UUID studentId;
    private String registrationId;
    private String studentName;
    private Long totalEnrollments;
    private Long activeEnrollments;
    private Long completedCourses;
    private Long withdrawnCourses;
    private Double overallAttendanceRate;
    private Long totalAbsences;
    private Long totalClassesAttended;
    private Long totalClasses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}