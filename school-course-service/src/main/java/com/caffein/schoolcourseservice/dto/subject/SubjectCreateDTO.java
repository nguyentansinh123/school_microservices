package com.caffein.schoolcourseservice.dto.subject;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectCreateDTO {

    @NotBlank(message = "Subject name is required")
    private String name;

    @NotBlank(message = "Department is required")
    private String department;
}