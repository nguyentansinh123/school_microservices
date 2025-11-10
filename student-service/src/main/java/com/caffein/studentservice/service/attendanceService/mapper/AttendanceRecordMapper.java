package com.caffein.studentservice.service.attendanceService.mapper;

import com.caffein.studentservice.dto.AttendanceRecordDTO;
import com.caffein.studentservice.model.AttendanceRecord;
import org.springframework.stereotype.Component;

@Component
public class AttendanceRecordMapper {

    public AttendanceRecordDTO toDTO(AttendanceRecord record) {
        if (record == null) return null;

        return AttendanceRecordDTO.builder()
                .id(record.getId())
                .studentId(record.getStudent() != null ? record.getStudent().getId() : null)
                .enrollmentId(record.getEnrollment() != null ? record.getEnrollment().getId() : null)
                .courseId(record.getCourseId())
                .courseName(record.getEnrollment() != null ? record.getEnrollment().getCourseName() : null)
                .date(record.getDate())
                .status(record.getStatus())
                .remarks(record.getRemarks())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}