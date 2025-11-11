package com.caffein.analyticservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentEvent {
    private UUID enrollmentId;
    private UUID studentId;
    private UUID courseId;
    private String courseName;
    private String academicYear;
    private String semester;
    private String status; // REGISTERED, WITHDRAWN, COMPLETED
    private Integer maxCapacity;
}