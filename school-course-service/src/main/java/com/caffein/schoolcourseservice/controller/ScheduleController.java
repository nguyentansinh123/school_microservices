package com.caffein.schoolcourseservice.controller;


import com.caffein.schoolcourseservice.dto.schedule.ScheduleCreateDTO;
import com.caffein.schoolcourseservice.dto.schedule.ScheduleDTO;
import com.caffein.schoolcourseservice.service.schedule.IScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cs")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "Course Schedule Management API")
public class ScheduleController {

    private final IScheduleService scheduleService;

    @PostMapping("/courses/{courseId}/schedules")
    @Operation(summary = "Add a new meeting time to a course")
    public ResponseEntity<ScheduleDTO> addScheduleToCourse(
            @PathVariable UUID courseId,
            @Valid @RequestBody ScheduleCreateDTO createDTO) {
        ScheduleDTO schedule = scheduleService.addScheduleToCourse(courseId, createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    @DeleteMapping("/schedules/{id}")
    @Operation(summary = "Remove a meeting time from a course")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/schedules/{id}")
    @Operation(summary = "Update a schedule")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable UUID id,
            @Valid @RequestBody ScheduleCreateDTO updateDTO) {
        ScheduleDTO updatedSchedule = scheduleService.updateSchedule(id, updateDTO);
        return ResponseEntity.ok(updatedSchedule);
    }
}