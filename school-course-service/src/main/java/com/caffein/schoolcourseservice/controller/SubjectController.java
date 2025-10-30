package com.caffein.schoolcourseservice.controller;

import com.caffein.schoolcourseservice.dto.subject.SubjectCreateDTO;
import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;
import com.caffein.schoolcourseservice.service.subjectService.ISubjectService;
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
@RequestMapping("/api/v1/cs/subjects")
@RequiredArgsConstructor
@Tag(name = "Subject", description = "Subject Management API")
public class SubjectController {

    private final ISubjectService subjectService;

    @PostMapping
    @Operation(summary = "Create a new subject")
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody SubjectCreateDTO createDTO) {
        SubjectDTO subject = subjectService.createSubject(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
    }

    @GetMapping
    @Operation(summary = "Get all subjects")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable UUID id) {
        SubjectDTO subject = subjectService.getSubjectById(id);
        return ResponseEntity.ok(subject);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a subject")
    public ResponseEntity<Void> deleteSubject(@PathVariable UUID id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}