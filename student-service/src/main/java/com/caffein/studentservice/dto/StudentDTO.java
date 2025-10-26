package com.caffein.studentservice.dto;

import com.caffein.studentservice.model.Student;
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
public class StudentDTO {
    private UUID id;
    private UUID userId;
    private String registrationId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String addressLine;
    private String city;
    private String stateProvince;
    private String postalCode;
    private Student.EnrollmentStatus enrollmentStatus;
    private List<GuardianDTO> guardians;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}