package com.caffein.studentservice.dto;

import com.caffein.studentservice.model.Enrollment;
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
public class EnrollmentDTO {
    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private String courseCode;
    private String courseName;
    private String academicYear;
    private String semester;
    private String grade;
    private Enrollment.EnrollmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}