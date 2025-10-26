package com.caffein.studentservice.service.enrollmentService;

import com.caffein.studentservice.dto.EnrollmentDTO;
import com.caffein.studentservice.request.EnrollmentRequest;

import java.util.List;
import java.util.UUID;

public interface IEnrollmentService {
    EnrollmentDTO enrollStudent(EnrollmentRequest request);
    void unenrollStudent(UUID studentId, UUID enrollmentId);
    List<EnrollmentDTO> getStudentEnrollments(UUID studentId);
}