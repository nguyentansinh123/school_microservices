package com.caffein.analyticservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceAnalyticsDTO {
    private UUID id;
    private UUID courseId;
    private String courseName;
    private LocalDate date;
    private Long totalStudents;
    private Long presentCount;
    private Long absentCount;
    private Long lateCount;
    private Long excusedCount;
    private Double attendanceRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}