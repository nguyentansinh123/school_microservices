package com.caffein.studentservice.service.attendanceService;

import com.caffein.studentservice.dto.AttendanceRecordCreateDTO;
import com.caffein.studentservice.dto.AttendanceRecordDTO;
import com.caffein.studentservice.dto.AttendanceRecordUpdateDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IAttendanceRecordService {

    AttendanceRecordDTO recordAttendance(AttendanceRecordCreateDTO createDTO);

    AttendanceRecordDTO updateAttendance(UUID recordId, AttendanceRecordUpdateDTO updateDTO);

    void deleteAttendance(UUID recordId);

    AttendanceRecordDTO getAttendanceById(UUID recordId);

    List<AttendanceRecordDTO> getStudentAttendance(UUID studentId);

    List<AttendanceRecordDTO> getStudentAttendanceForCourse(UUID studentId, UUID courseId);

    List<AttendanceRecordDTO> getStudentAttendanceDateRange(UUID studentId, LocalDate startDate, LocalDate endDate);

    List<AttendanceRecordDTO> getCourseAttendanceForDate(UUID courseId, LocalDate date);

    Map<String, Long> getAttendanceStatistics(UUID studentId, UUID courseId);
}