package com.caffein.studentservice.dto;

import com.caffein.studentservice.model.AttendanceRecord;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecordUpdateDTO {

    @NotNull(message = "Attendance status is required")
    private AttendanceRecord.AttendanceStatus status;

    private String remarks;
}