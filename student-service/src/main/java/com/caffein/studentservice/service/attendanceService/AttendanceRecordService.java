package com.caffein.studentservice.service.attendanceService;


import com.caffein.studentservice.dto.AttendanceRecordCreateDTO;
import com.caffein.studentservice.dto.AttendanceRecordDTO;
import com.caffein.studentservice.dto.AttendanceRecordUpdateDTO;
import com.caffein.studentservice.model.AttendanceRecord;
import com.caffein.studentservice.model.Enrollment;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.repository.AttendanceRecordRepository;
import com.caffein.studentservice.repository.EnrollmentRepository;
import com.caffein.studentservice.repository.StudentRepository;
import com.caffein.studentservice.service.attendanceService.mapper.AttendanceRecordMapper;
import jakarta.persistence.EntityNotFoundException;
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
public class AttendanceRecordService implements IAttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRecordMapper attendanceRecordMapper;

    @Override
    @Transactional
    public AttendanceRecordDTO recordAttendance(AttendanceRecordCreateDTO createDTO) {
        log.info("Recording attendance for student {} in course {} on {}",
                createDTO.getStudentId(), createDTO.getCourseId(), createDTO.getDate());

        // Validate student exists
        Student student = studentRepository.findById(createDTO.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + createDTO.getStudentId()));

        // Validate enrollment exists
        Enrollment enrollment = enrollmentRepository.findById(createDTO.getEnrollmentId())
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found with ID: " + createDTO.getEnrollmentId()));

        // Verify enrollment belongs to student and course
        if (!enrollment.getStudent().getId().equals(createDTO.getStudentId())) {
            throw new IllegalArgumentException("Enrollment does not belong to the specified student");
        }

        if (!enrollment.getCourseId().equals(createDTO.getCourseId())) {
            throw new IllegalArgumentException("Enrollment does not match the specified course");
        }

        // Check if attendance already exists for this date
        if (attendanceRecordRepository.existsByStudentIdAndCourseIdAndDate(
                createDTO.getStudentId(), createDTO.getCourseId(), createDTO.getDate())) {
            throw new IllegalStateException("Attendance record already exists for this student, course, and date");
        }

        // Create attendance record
        AttendanceRecord record = AttendanceRecord.builder()
                .student(student)
                .enrollment(enrollment)
                .courseId(createDTO.getCourseId())
                .date(createDTO.getDate())
                .status(createDTO.getStatus())
                .remarks(createDTO.getRemarks())
                .build();

        AttendanceRecord savedRecord = attendanceRecordRepository.save(record);
        log.info("Successfully recorded attendance for student {} - Status: {}",
                student.getId(), createDTO.getStatus());

        return attendanceRecordMapper.toDTO(savedRecord);
    }

    @Override
    @Transactional
    public AttendanceRecordDTO updateAttendance(UUID recordId, AttendanceRecordUpdateDTO updateDTO) {
        log.info("Updating attendance record {}", recordId);

        AttendanceRecord record = attendanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Attendance record not found with ID: " + recordId));

        record.setStatus(updateDTO.getStatus());
        record.setRemarks(updateDTO.getRemarks());

        AttendanceRecord updatedRecord = attendanceRecordRepository.save(record);
        log.info("Successfully updated attendance record {} - New status: {}", recordId, updateDTO.getStatus());

        return attendanceRecordMapper.toDTO(updatedRecord);
    }

    @Override
    @Transactional
    public void deleteAttendance(UUID recordId) {
        log.info("Deleting attendance record {}", recordId);

        AttendanceRecord record = attendanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Attendance record not found with ID: " + recordId));

        attendanceRecordRepository.delete(record);
        log.info("Successfully deleted attendance record {}", recordId);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceRecordDTO getAttendanceById(UUID recordId) {
        log.info("Fetching attendance record {}", recordId);

        AttendanceRecord record = attendanceRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Attendance record not found with ID: " + recordId));

        return attendanceRecordMapper.toDTO(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getStudentAttendance(UUID studentId) {
        log.info("Fetching all attendance records for student {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found with ID: " + studentId);
        }

        List<AttendanceRecord> records = attendanceRecordRepository.findByStudentId(studentId);
        return records.stream()
                .map(attendanceRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getStudentAttendanceForCourse(UUID studentId, UUID courseId) {
        log.info("Fetching attendance records for student {} in course {}", studentId, courseId);

        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found with ID: " + studentId);
        }

        List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndCourseId(studentId, courseId);
        return records.stream()
                .map(attendanceRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getStudentAttendanceDateRange(UUID studentId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching attendance records for student {} between {} and {}", studentId, startDate, endDate);

        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found with ID: " + studentId);
        }

        List<AttendanceRecord> records = attendanceRecordRepository.findByStudentIdAndDateBetween(studentId, startDate, endDate);
        return records.stream()
                .map(attendanceRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getCourseAttendanceForDate(UUID courseId, LocalDate date) {
        log.info("Fetching attendance records for course {} on {}", courseId, date);

        List<AttendanceRecord> records = attendanceRecordRepository.findByCourseIdAndDate(courseId, date);
        return records.stream()
                .map(attendanceRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getAttendanceStatistics(UUID studentId, UUID courseId) {
        log.info("Calculating attendance statistics for student {} in course {}", studentId, courseId);

        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found with ID: " + studentId);
        }

        Map<String, Long> statistics = new HashMap<>();

        statistics.put("present", attendanceRecordRepository.countByStudentIdAndCourseIdAndStatus(
                studentId, courseId, AttendanceRecord.AttendanceStatus.PRESENT));

        statistics.put("absent", attendanceRecordRepository.countByStudentIdAndCourseIdAndStatus(
                studentId, courseId, AttendanceRecord.AttendanceStatus.ABSENT));

        statistics.put("late", attendanceRecordRepository.countByStudentIdAndCourseIdAndStatus(
                studentId, courseId, AttendanceRecord.AttendanceStatus.LATE));

        statistics.put("excused", attendanceRecordRepository.countByStudentIdAndCourseIdAndStatus(
                studentId, courseId, AttendanceRecord.AttendanceStatus.EXCUSED));

        long total = statistics.values().stream().mapToLong(Long::longValue).sum();
        statistics.put("total", total);

        if (total > 0) {
            double attendanceRate = (statistics.get("present") + statistics.get("late")) * 100.0 / total;
            statistics.put("attendanceRate", Math.round(attendanceRate));
        } else {
            statistics.put("attendanceRate", 0L);
        }

        return statistics;
    }
}