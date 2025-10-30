package com.caffein.schoolcourseservice.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseCreateDTO {

    @NotNull(message = "Subject ID is required")
    private UUID subjectId;

    @NotNull(message = "Teacher ID is required")
    private UUID teacherId;

    @NotBlank(message = "Course name is required")
    private String name;

    private String description;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotNull(message = "Max capacity is required")
    @Positive(message = "Max capacity must be positive")
    private Integer maxCapacity;
}