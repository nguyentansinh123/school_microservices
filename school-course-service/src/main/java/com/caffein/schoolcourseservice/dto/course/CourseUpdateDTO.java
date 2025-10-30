package com.caffein.schoolcourseservice.dto.course;

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
public class CourseUpdateDTO {

    private UUID teacherId;
    private String name;
    private String description;
    private String academicYear;
    private String semester;

    @Positive(message = "Max capacity must be positive")
    private Integer maxCapacity;
}