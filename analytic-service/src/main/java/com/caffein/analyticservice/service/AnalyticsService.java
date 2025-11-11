package com.caffein.analyticservice.service;


import com.caffein.analyticservice.dto.AttendanceAnalyticsDTO;
import com.caffein.analyticservice.dto.DashboardSummaryDTO;
import com.caffein.analyticservice.dto.EnrollmentAnalyticsDTO;
import com.caffein.analyticservice.dto.StudentAnalyticsDTO;
import com.caffein.analyticservice.kafka.AttendanceRecordedEvent;
import com.caffein.analyticservice.kafka.EnrollmentEvent;
import com.caffein.analyticservice.model.AttendanceAnalytics;
import com.caffein.analyticservice.model.EnrollmentAnalytics;
import com.caffein.analyticservice.model.StudentAnalytics;
import com.caffein.analyticservice.repository.AttendanceAnalyticsRepository;
import com.caffein.analyticservice.repository.EnrollmentAnalyticsRepository;
import com.caffein.analyticservice.repository.StudentAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final EnrollmentAnalyticsRepository enrollmentAnalyticsRepository;
    private final AttendanceAnalyticsRepository attendanceAnalyticsRepository;
    private final StudentAnalyticsRepository studentAnalyticsRepository;

    @Transactional
    public void processEnrollmentEvent(EnrollmentEvent event) {
        log.info("Processing enrollment event for course: {}", event.getCourseId());

        EnrollmentAnalytics analytics = enrollmentAnalyticsRepository.findByCourseId(event.getCourseId())
                .orElse(EnrollmentAnalytics.builder()
                        .courseId(event.getCourseId())
                        .courseName(event.getCourseName())
                        .academicYear(event.getAcademicYear())
                        .semester(event.getSemester())
                        .totalCapacity(event.getMaxCapacity())
                        .totalEnrolled(0L)
                        .activeEnrollments(0L)
                        .withdrawnEnrollments(0L)
                        .completedEnrollments(0L)
                        .build());

        switch (event.getStatus()) {
            case "REGISTERED":
                analytics.setTotalEnrolled(analytics.getTotalEnrolled() + 1);
                analytics.setActiveEnrollments(analytics.getActiveEnrollments() + 1);
                break;
            case "WITHDRAWN":
                analytics.setActiveEnrollments(Math.max(0, analytics.getActiveEnrollments() - 1));
                analytics.setWithdrawnEnrollments(analytics.getWithdrawnEnrollments() + 1);
                break;
            case "COMPLETED":
                analytics.setActiveEnrollments(Math.max(0, analytics.getActiveEnrollments() - 1));
                analytics.setCompletedEnrollments(analytics.getCompletedEnrollments() + 1);
                break;
        }

        if (analytics.getTotalCapacity() != null && analytics.getTotalCapacity() > 0) {
            double rate = (analytics.getTotalEnrolled() * 100.0) / analytics.getTotalCapacity();
            analytics.setEnrollmentRate(Math.round(rate * 100.0) / 100.0);
        }

        enrollmentAnalyticsRepository.save(analytics);
        log.info("Updated enrollment analytics for course: {}", event.getCourseId());
    }

    @Transactional
    public void processAttendanceEvent(AttendanceRecordedEvent event) {
        log.info("Processing attendance event for course: {} on date: {}", event.getCourseId(), event.getDate());

        AttendanceAnalytics analytics = attendanceAnalyticsRepository
                .findByCourseIdAndDate(event.getCourseId(), event.getDate())
                .orElse(AttendanceAnalytics.builder()
                        .courseId(event.getCourseId())
                        .courseName(event.getCourseName())
                        .date(event.getDate())
                        .totalStudents(0L)
                        .presentCount(0L)
                        .absentCount(0L)
                        .lateCount(0L)
                        .excusedCount(0L)
                        .build());

        // Increment appropriate counter
        switch (event.getStatus()) {
            case "PRESENT":
                analytics.setPresentCount(analytics.getPresentCount() + 1);
                break;
            case "ABSENT":
                analytics.setAbsentCount(analytics.getAbsentCount() + 1);
                break;
            case "LATE":
                analytics.setLateCount(analytics.getLateCount() + 1);
                break;
            case "EXCUSED":
                analytics.setExcusedCount(analytics.getExcusedCount() + 1);
                break;
        }

        // Calculate total and attendance rate
        long total = analytics.getPresentCount() + analytics.getAbsentCount() +
                analytics.getLateCount() + analytics.getExcusedCount();
        analytics.setTotalStudents(total);

        if (total > 0) {
            double rate = ((analytics.getPresentCount() + analytics.getLateCount()) * 100.0) / total;
            analytics.setAttendanceRate(Math.round(rate * 100.0) / 100.0);
        }

        attendanceAnalyticsRepository.save(analytics);

        // Update student analytics
        updateStudentAnalytics(event.getStudentId());

        log.info("Updated attendance analytics for course: {} on {}", event.getCourseId(), event.getDate());
    }

    @Transactional
    protected void updateStudentAnalytics(UUID studentId) {
        log.info("Student analytics update triggered for: {}", studentId);
    }

    public DashboardSummaryDTO getDashboardSummary() {
        Long totalStudents = studentAnalyticsRepository.getTotalStudents();
        Long totalEnrollments = enrollmentAnalyticsRepository.getTotalEnrollments();
        Double avgAttendance = studentAnalyticsRepository.getAverageAttendanceRate();
        Long absencesToday = attendanceAnalyticsRepository.getTotalAbsencesByDate(LocalDate.now());

        // Get enrollment counts by status
        List<EnrollmentAnalytics> allEnrollments = enrollmentAnalyticsRepository.findAll();
        Map<String, Long> enrollmentsByStatus = new HashMap<>();
        enrollmentsByStatus.put("active", allEnrollments.stream()
                .mapToLong(EnrollmentAnalytics::getActiveEnrollments).sum());
        enrollmentsByStatus.put("completed", allEnrollments.stream()
                .mapToLong(EnrollmentAnalytics::getCompletedEnrollments).sum());
        enrollmentsByStatus.put("withdrawn", allEnrollments.stream()
                .mapToLong(EnrollmentAnalytics::getWithdrawnEnrollments).sum());

        // Get attendance trends for last 7 days
        LocalDate today = LocalDate.now();
        Map<String, Double> attendanceTrends = new HashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Double rate = attendanceAnalyticsRepository.getAverageAttendanceRate(date, date);
            attendanceTrends.put(date.toString(), rate != null ? rate : 0.0);
        }

        return DashboardSummaryDTO.builder()
                .totalStudents(totalStudents != null ? totalStudents : 0L)
                .totalCourses((long) allEnrollments.size())
                .totalEnrollments(totalEnrollments != null ? totalEnrollments : 0L)
                .averageAttendanceRate(avgAttendance != null ? Math.round(avgAttendance * 100.0) / 100.0 : 0.0)
                .totalAbsencesToday(absencesToday != null ? absencesToday : 0L)
                .enrollmentsByStatus(enrollmentsByStatus)
                .attendanceTrends(attendanceTrends)
                .build();
    }

    public List<EnrollmentAnalyticsDTO> getEnrollmentAnalyticsByTerm(String academicYear, String semester) {
        return enrollmentAnalyticsRepository.findByAcademicYearAndSemester(academicYear, semester)
                .stream()
                .map(this::toEnrollmentDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceAnalyticsDTO> getAttendanceAnalyticsByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceAnalyticsRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(this::toAttendanceDTO)
                .collect(Collectors.toList());
    }

    public StudentAnalyticsDTO getStudentAnalytics(UUID studentId) {
        return studentAnalyticsRepository.findByStudentId(studentId)
                .map(this::toStudentDTO)
                .orElse(null);
    }

    private EnrollmentAnalyticsDTO toEnrollmentDTO(EnrollmentAnalytics entity) {
        return EnrollmentAnalyticsDTO.builder()
                .id(entity.getId())
                .courseId(entity.getCourseId())
                .courseName(entity.getCourseName())
                .academicYear(entity.getAcademicYear())
                .semester(entity.getSemester())
                .totalEnrolled(entity.getTotalEnrolled())
                .totalCapacity(entity.getTotalCapacity())
                .enrollmentRate(entity.getEnrollmentRate())
                .activeEnrollments(entity.getActiveEnrollments())
                .withdrawnEnrollments(entity.getWithdrawnEnrollments())
                .completedEnrollments(entity.getCompletedEnrollments())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private AttendanceAnalyticsDTO toAttendanceDTO(AttendanceAnalytics entity) {
        return AttendanceAnalyticsDTO.builder()
                .id(entity.getId())
                .courseId(entity.getCourseId())
                .courseName(entity.getCourseName())
                .date(entity.getDate())
                .totalStudents(entity.getTotalStudents())
                .presentCount(entity.getPresentCount())
                .absentCount(entity.getAbsentCount())
                .lateCount(entity.getLateCount())
                .excusedCount(entity.getExcusedCount())
                .attendanceRate(entity.getAttendanceRate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private StudentAnalyticsDTO toStudentDTO(StudentAnalytics entity) {
        return StudentAnalyticsDTO.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .registrationId(entity.getRegistrationId())
                .studentName(entity.getStudentName())
                .totalEnrollments(entity.getTotalEnrollments())
                .activeEnrollments(entity.getActiveEnrollments())
                .completedCourses(entity.getCompletedCourses())
                .withdrawnCourses(entity.getWithdrawnCourses())
                .overallAttendanceRate(entity.getOverallAttendanceRate())
                .totalAbsences(entity.getTotalAbsences())
                .totalClassesAttended(entity.getTotalClassesAttended())
                .totalClasses(entity.getTotalClasses())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}