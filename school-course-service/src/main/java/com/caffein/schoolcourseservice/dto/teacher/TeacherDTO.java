package com.caffein.schoolcourseservice.dto.teacher;

import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDTO {
    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private String officeLocation;
    private LocalDate hireDate;
    private List<SubjectDTO> subjects;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}