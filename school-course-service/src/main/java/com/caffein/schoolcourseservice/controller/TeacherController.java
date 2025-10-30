package com.caffein.schoolcourseservice.controller;

import com.caffein.schoolcourseservice.dto.teacher.TeacherDTO;
import com.caffein.schoolcourseservice.dto.teacher.TeacherUpdateDTO;
import com.caffein.schoolcourseservice.service.teacherService.ITeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cs/teachers")
@RequiredArgsConstructor
@Tag(name = "Teacher", description = "Teacher Management API")
public class TeacherController {

    private final ITeacherService teacherService;

    @GetMapping
    @Operation(summary = "Get all teachers")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current teacher profile")
    public ResponseEntity<TeacherDTO> getCurrentTeacher(@RequestHeader("X-User-Id") String userId) {
        TeacherDTO teacher = teacherService.getTeacherByUserId(UUID.fromString(userId));
        return ResponseEntity.ok(teacher);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher profile")
    public ResponseEntity<TeacherDTO> updateTeacher(
            @PathVariable UUID id,
            @Valid @RequestBody TeacherUpdateDTO updateDTO) {
        TeacherDTO updatedTeacher = teacherService.updateTeacher(id, updateDTO);
        return ResponseEntity.ok(updatedTeacher);
    }

    @PostMapping("/{teacherId}/subjects/{subjectId}")
    @Operation(summary = "Assign a subject to a teacher")
    public ResponseEntity<Void> assignSubjectToTeacher(
            @PathVariable UUID teacherId,
            @PathVariable UUID subjectId) {
        teacherService.assignSubjectToTeacher(teacherId, subjectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teacherId}/subjects/{subjectId}")
    @Operation(summary = "Remove a subject from a teacher")
    public ResponseEntity<Void> removeSubjectFromTeacher(
            @PathVariable UUID teacherId,
            @PathVariable UUID subjectId) {
        teacherService.removeSubjectFromTeacher(teacherId, subjectId);
        return ResponseEntity.ok().build();
    }
}