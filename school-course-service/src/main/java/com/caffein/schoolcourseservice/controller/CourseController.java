package com.caffein.schoolcourseservice.controller;


import com.caffein.schoolcourseservice.dto.course.CourseCreateDTO;
import com.caffein.schoolcourseservice.dto.course.CourseDTO;
import com.caffein.schoolcourseservice.dto.course.CourseUpdateDTO;
import com.caffein.schoolcourseservice.service.courseService.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cs/courses")
@RequiredArgsConstructor
@Tag(name = "Course", description = "Course Management API")
public class CourseController {

    private final ICourseService courseService;

    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseCreateDTO createDTO) {
        CourseDTO course = courseService.createCourse(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @GetMapping
    @Operation(summary = "Get all courses with optional filtering")
    public ResponseEntity<List<CourseDTO>> getAllCourses(
            @Parameter(description = "Filter by subject name") @RequestParam(required = false) String subject,
            @Parameter(description = "Filter by teacher name") @RequestParam(required = false) String teacher,
            @Parameter(description = "Filter by academic year") @RequestParam(required = false) String academicYear,
            @Parameter(description = "Filter by semester") @RequestParam(required = false) String semester) {
        List<CourseDTO> courses = courseService.getAllCourses(subject, teacher, academicYear, semester);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID with full details including schedules")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable UUID id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a course")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable UUID id,
            @Valid @RequestBody CourseUpdateDTO updateDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, updateDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete/cancel a course")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}