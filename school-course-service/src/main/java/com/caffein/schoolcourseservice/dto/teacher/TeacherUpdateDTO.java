package com.caffein.schoolcourseservice.dto.teacher;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherUpdateDTO {

    @NotBlank(message = "Department is required")
    private String department;

    private String officeLocation;
    private LocalDate hireDate;
    private String phoneNumber;
}