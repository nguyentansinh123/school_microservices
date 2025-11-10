package com.caffein.studentservice.controller;

import com.caffein.studentservice.dto.AttendanceRecordCreateDTO;
import com.caffein.studentservice.dto.AttendanceRecordDTO;
import com.caffein.studentservice.dto.AttendanceRecordUpdateDTO;
import com.caffein.studentservice.service.attendanceService.IAttendanceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ss/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "Attendance Record Management API")
public class AttendanceController {

    private final IAttendanceRecordService attendanceRecordService;

    @PostMapping
    @Operation(summary = "Record attendance for a student")
    public ResponseEntity<AttendanceRecordDTO> recordAttendance(@Valid @RequestBody AttendanceRecordCreateDTO createDTO) {
        AttendanceRecordDTO record = attendanceRecordService.recordAttendance(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PutMapping("/{recordId}")
    @Operation(summary = "Update an attendance record")
    public ResponseEntity<AttendanceRecordDTO> updateAttendance(
            @PathVariable UUID recordId,
            @Valid @RequestBody AttendanceRecordUpdateDTO updateDTO) {
        AttendanceRecordDTO record = attendanceRecordService.updateAttendance(recordId, updateDTO);
        return ResponseEntity.ok(record);
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "Delete an attendance record")
    public ResponseEntity<Void> deleteAttendance(@PathVariable UUID recordId) {
        attendanceRecordService.deleteAttendance(recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{recordId}")
    @Operation(summary = "Get attendance record by ID")
    public ResponseEntity<AttendanceRecordDTO> getAttendanceById(@PathVariable UUID recordId) {
        AttendanceRecordDTO record = attendanceRecordService.getAttendanceById(recordId);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/students/{studentId}")
    @Operation(summary = "Get all attendance records for a student")
    public ResponseEntity<List<AttendanceRecordDTO>> getStudentAttendance(@PathVariable UUID studentId) {
        List<AttendanceRecordDTO> records = attendanceRecordService.getStudentAttendance(studentId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/students/{studentId}/courses/{courseId}")
    @Operation(summary = "Get attendance records for a student in a specific course")
    public ResponseEntity<List<AttendanceRecordDTO>> getStudentAttendanceForCourse(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId) {
        List<AttendanceRecordDTO> records = attendanceRecordService.getStudentAttendanceForCourse(studentId, courseId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/students/{studentId}/range")
    @Operation(summary = "Get attendance records for a student within a date range")
    public ResponseEntity<List<AttendanceRecordDTO>> getStudentAttendanceDateRange(
            @PathVariable UUID studentId,
            @Parameter(description = "Start date (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceRecordDTO> records = attendanceRecordService.getStudentAttendanceDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/courses/{courseId}/date/{date}")
    @Operation(summary = "Get all attendance records for a course on a specific date")
    public ResponseEntity<List<AttendanceRecordDTO>> getCourseAttendanceForDate(
            @PathVariable UUID courseId,
            @Parameter(description = "Date (yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceRecordDTO> records = attendanceRecordService.getCourseAttendanceForDate(courseId, date);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/students/{studentId}/courses/{courseId}/statistics")
    @Operation(summary = "Get attendance statistics for a student in a course")
    public ResponseEntity<Map<String, Long>> getAttendanceStatistics(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId) {
        Map<String, Long> statistics = attendanceRecordService.getAttendanceStatistics(studentId, courseId);
        return ResponseEntity.ok(statistics);
    }
}