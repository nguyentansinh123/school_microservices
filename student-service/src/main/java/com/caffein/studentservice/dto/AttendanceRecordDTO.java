package com.caffein.studentservice.dto;

import com.caffein.studentservice.model.AttendanceRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecordDTO {
    private UUID id;
    private UUID studentId;
    private UUID enrollmentId;
    private UUID courseId;
    private String courseName;
    private LocalDate date;
    private AttendanceRecord.AttendanceStatus status;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}