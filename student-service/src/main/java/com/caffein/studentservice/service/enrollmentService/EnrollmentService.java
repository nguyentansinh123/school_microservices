package com.caffein.studentservice.service.enrollmentService;

import com.caffein.studentservice.dto.EnrollmentDTO;
import com.caffein.studentservice.model.Enrollment;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.repository.EnrollmentRepository;
import com.caffein.studentservice.repository.StudentRepository;
import com.caffein.studentservice.request.EnrollmentRequest;
import com.caffein.studentservice.service.enrollmentService.mapper.EnrollmentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService implements IEnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentDTO enrollStudent(EnrollmentRequest request) {
        log.info("Enrolling student {} in course {}", request.getStudentId(), request.getCourseId());

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + request.getStudentId()));

        if (student.getEnrollmentStatus() != Student.EnrollmentStatus.ACTIVE) {
            throw new IllegalStateException("Student must be in ACTIVE status to enroll in courses");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), request.getCourseId())) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courseId(request.getCourseId())
                .courseCode(request.getCourseCode())
                .courseName(request.getCourseName())
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .status(Enrollment.EnrollmentStatus.REGISTERED)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Successfully enrolled student {} in course {}", student.getId(), request.getCourseId());

        return enrollmentMapper.toDTO(savedEnrollment);
    }


    @Override
    @Transactional
    public void unenrollStudent(UUID studentId, UUID enrollmentId) {
        log.info("Unenrolling student {} from enrollment {}", studentId, enrollmentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found with ID: " + enrollmentId));

        // Verify the enrollment belongs to the student
        if (!enrollment.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("Enrollment does not belong to the specified student");
        }

        // Check if enrollment can be withdrawn
        if (enrollment.getStatus() == Enrollment.EnrollmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot unenroll from a completed course");
        }

        enrollment.setStatus(Enrollment.EnrollmentStatus.WITHDRAWN);
        enrollmentRepository.save(enrollment);

        log.info("Successfully unenrolled student {} from course {}",
                studentId, enrollment.getCourseCode());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getStudentEnrollments(UUID studentId) {
        log.info("Fetching enrollments for student {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found with ID: " + studentId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(enrollmentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
