package com.caffein.studentservice.dto;

import com.caffein.studentservice.model.AttendanceRecord;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecordCreateDTO {

    @NotNull(message = "Student ID is required")
    private UUID studentId;

    @NotNull(message = "Enrollment ID is required")
    private UUID enrollmentId;

    @NotNull(message = "Course ID is required")
    private UUID courseId;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @NotNull(message = "Attendance status is required")
    private AttendanceRecord.AttendanceStatus status;

    private String remarks;
}