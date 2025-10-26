package com.caffein.studentservice.service.enrollmentService.mapper;

import com.caffein.studentservice.dto.EnrollmentDTO;
import com.caffein.studentservice.model.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public EnrollmentDTO toDTO(Enrollment enrollment) {
        return EnrollmentDTO.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .courseId(enrollment.getCourseId())
                .courseCode(enrollment.getCourseCode())
                .courseName(enrollment.getCourseName())
                .academicYear(enrollment.getAcademicYear())
                .semester(enrollment.getSemester())
                .grade(enrollment.getGrade())
                .status(enrollment.getStatus())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }
}