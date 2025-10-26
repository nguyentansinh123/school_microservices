package com.caffein.studentservice.controller;

import com.caffein.studentservice.dto.StudentDTO;
import com.caffein.studentservice.dto.StudentUpdateDTO;
import com.caffein.studentservice.dto.GuardianDTO;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.model.Guardian;
import com.caffein.studentservice.service.studentService.IStudentService;
import com.caffein.studentservice.service.guardianService.IGuardianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ss/students")
@RequiredArgsConstructor
@Tag(name = "Student", description = "Student Management API")
public class StudentController {

    private final IStudentService studentService;
    private final IGuardianService guardianService;

    @GetMapping
    @Operation(summary = "Get all students with search functionality")
    public ResponseEntity<Page<StudentDTO>> getAllStudents(
            @Parameter(description = "Search by name, email, or registration ID")
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<StudentDTO> students = studentService.getAllStudents(search, pageable);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID with full profile")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable UUID id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student profile and activate enrollment")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable UUID id,
            @Valid @RequestBody StudentUpdateDTO updateDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(id, updateDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    @PostMapping("/{studentId}/guardians")
    @Operation(summary = "Add a guardian to a student")
    public ResponseEntity<GuardianDTO> addGuardian(
            @PathVariable UUID studentId,
            @Valid @RequestBody GuardianDTO guardianDTO) {
        GuardianDTO guardian = guardianService.addGuardianToStudent(studentId, guardianDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardian);
    }

    @GetMapping("/{studentId}/guardians")
    @Operation(summary = "Get all guardians for a student")
    public ResponseEntity<List<GuardianDTO>> getStudentGuardians(@PathVariable UUID studentId) {
        List<GuardianDTO> guardians = guardianService.getGuardiansByStudentId(studentId);
        return ResponseEntity.ok(guardians);
    }
}