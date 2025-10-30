package com.caffein.schoolcourseservice.dto.teacher.mapper;

import com.caffein.schoolcourseservice.dto.subject.mapper.SubjectMapper;
import com.caffein.schoolcourseservice.dto.teacher.TeacherDTO;
import com.caffein.schoolcourseservice.model.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TeacherMapper {

    private final SubjectMapper subjectMapper;

    public TeacherDTO toDTO(Teacher teacher) {
        if (teacher == null) return null;

        return TeacherDTO.builder()
                .id(teacher.getId())
                .userId(teacher.getUserId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phoneNumber(teacher.getPhoneNumber())
                .department(teacher.getDepartment())
                .officeLocation(teacher.getOfficeLocation())
                .hireDate(teacher.getHireDate())
                .subjects(teacher.getSubjects() != null ?
                        teacher.getSubjects().stream()
                                .map(subjectMapper::toDTOWithoutRelations)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .build();
    }

    public TeacherDTO toDTOWithoutRelations(Teacher teacher) {
        if (teacher == null) return null;

        return TeacherDTO.builder()
                .id(teacher.getId())
                .userId(teacher.getUserId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phoneNumber(teacher.getPhoneNumber())
                .department(teacher.getDepartment())
                .officeLocation(teacher.getOfficeLocation())
                .hireDate(teacher.getHireDate())
                .createdAt(teacher.getCreatedAt())
                .updatedAt(teacher.getUpdatedAt())
                .build();
    }
}