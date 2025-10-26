package com.caffein.studentservice.controller;

import com.caffein.studentservice.dto.EnrollmentDTO;
import com.caffein.studentservice.request.EnrollmentRequest;
import com.caffein.studentservice.service.enrollmentService.IEnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ss/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment", description = "Course Enrollment Management API")
public class EnrollmentController {

    private final IEnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<EnrollmentDTO> enrollStudent(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentDTO enrollment = enrollmentService.enrollStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @DeleteMapping("/{enrollmentId}/students/{studentId}")
    @Operation(summary = "Unenroll a student from a course")
    public ResponseEntity<Void> unenrollStudent(
            @PathVariable UUID studentId,
            @PathVariable UUID enrollmentId) {
        enrollmentService.unenrollStudent(studentId, enrollmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students/{studentId}")
    @Operation(summary = "Get all enrollments for a student")
    public ResponseEntity<List<EnrollmentDTO>> getStudentEnrollments(@PathVariable UUID studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getStudentEnrollments(studentId);
        return ResponseEntity.ok(enrollments);
    }
}