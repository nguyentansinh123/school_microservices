package com.caffein.analyticservice.kafka;

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
public class AttendanceRecordedEvent {
    private UUID attendanceId;
    private UUID studentId;
    private UUID courseId;
    private String courseName;
    private LocalDate date;
    private String status; // PRESENT, ABSENT, LATE, EXCUSED
}