package com.caffein.analyticservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {
    private Long totalStudents;
    private Long totalCourses;
    private Long totalEnrollments;
    private Double averageAttendanceRate;
    private Long totalAbsencesToday;
    private Map<String, Long> enrollmentsByStatus;
    private Map<String, Double> attendanceTrends; // Last 7 days
}