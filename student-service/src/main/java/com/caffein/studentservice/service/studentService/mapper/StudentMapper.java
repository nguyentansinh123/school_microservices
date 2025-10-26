package com.caffein.studentservice.service.studentService.mapper;

import com.caffein.studentservice.dto.StudentDTO;
import com.caffein.studentservice.dto.StudentUpdateDTO;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.service.guardianService.mapper.GuardianMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final GuardianMapper guardianMapper;

    public StudentDTO toDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .userId(student.getUserId())
                .registrationId(student.getRegistrationId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .dateOfBirth(student.getDateOfBirth())
                .addressLine(student.getAddressLine())
                .city(student.getCity())
                .stateProvince(student.getStateProvince())
                .postalCode(student.getPostalCode())
                .enrollmentStatus(student.getEnrollmentStatus())
                .guardians(student.getGuardians() != null ?
                        student.getGuardians().stream()
                                .map(guardianMapper::toDTO)
                                .toList() : null)
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }

    public void updateStudentFromDTO(Student student, StudentUpdateDTO updateDTO) {
        if (updateDTO.getFirstName() != null) {
            student.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getLastName() != null) {
            student.setLastName(updateDTO.getLastName());
        }
        if (updateDTO.getEmail() != null) {
            student.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPhoneNumber() != null) {
            student.setPhoneNumber(updateDTO.getPhoneNumber());
        }
        if (updateDTO.getDateOfBirth() != null) {
            student.setDateOfBirth(updateDTO.getDateOfBirth());
        }
        if (updateDTO.getAddressLine() != null) {
            student.setAddressLine(updateDTO.getAddressLine());
        }
        if (updateDTO.getCity() != null) {
            student.setCity(updateDTO.getCity());
        }
        if (updateDTO.getStateProvince() != null) {
            student.setStateProvince(updateDTO.getStateProvince());
        }
        if (updateDTO.getPostalCode() != null) {
            student.setPostalCode(updateDTO.getPostalCode());
        }
    }
}