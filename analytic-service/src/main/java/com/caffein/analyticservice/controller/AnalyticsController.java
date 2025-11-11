package com.caffein.analyticservice.controller;

import com.caffein.analyticservice.dto.AttendanceAnalyticsDTO;
import com.caffein.analyticservice.dto.DashboardSummaryDTO;
import com.caffein.analyticservice.dto.EnrollmentAnalyticsDTO;
import com.caffein.analyticservice.dto.StudentAnalyticsDTO;
import com.caffein.analyticservice.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Analytics and Reporting API")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard summary with key metrics")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(analyticsService.getDashboardSummary());
    }

    @GetMapping("/enrollments")
    @Operation(summary = "Get enrollment analytics by academic term")
    public ResponseEntity<List<EnrollmentAnalyticsDTO>> getEnrollmentAnalytics(
            @RequestParam String academicYear,
            @RequestParam String semester) {
        return ResponseEntity.ok(analyticsService.getEnrollmentAnalyticsByTerm(academicYear, semester));
    }

    @GetMapping("/attendance")
    @Operation(summary = "Get attendance analytics by date range")
    public ResponseEntity<List<AttendanceAnalyticsDTO>> getAttendanceAnalytics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.getAttendanceAnalyticsByDateRange(startDate, endDate));
    }

    @GetMapping("/students/{studentId}")
    @Operation(summary = "Get analytics for a specific student")
    public ResponseEntity<StudentAnalyticsDTO> getStudentAnalytics(@PathVariable UUID studentId) {
        StudentAnalyticsDTO analytics = analyticsService.getStudentAnalytics(studentId);
        return analytics != null ? ResponseEntity.ok(analytics) : ResponseEntity.notFound().build();
    }
}