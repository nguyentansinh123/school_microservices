package com.caffein.studentservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentRequest {
    @NotNull(message = "Student ID is required")
    private UUID studentId;

    @NotNull(message = "Course ID is required")
    private UUID courseId;

    private String courseCode;
    private String courseName;
    private String academicYear;
    private String semester;
}