package com.caffein.studentservice.service.enrollmentService;

import com.caffein.studentservice.dto.EnrollmentDTO;
import com.caffein.studentservice.dto.SubjectDTO;
import com.caffein.studentservice.grpc.CourseInformationServiceGrpc;
import com.caffein.studentservice.model.Enrollment;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.repository.EnrollmentRepository;
import com.caffein.studentservice.repository.StudentRepository;
import com.caffein.studentservice.request.EnrollmentRequest;
import com.caffein.studentservice.service.enrollmentService.mapper.EnrollmentMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EnrollmentService implements IEnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentMapper enrollmentMapper;

    private final CourseInformationServiceGrpc.CourseInformationServiceBlockingStub courseInformationServiceBlockingStub;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            EnrollmentMapper enrollmentMapper,
            @Value("${school.course.service.address:localhost}") String serverAddress,
            @Value("${school.course.service.grpc.port:4312}") int serverPort) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.enrollmentMapper = enrollmentMapper;

        log.info("Connecting to School Course Service GRPC at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();
        this.courseInformationServiceBlockingStub = CourseInformationServiceGrpc.newBlockingStub(channel);
    }

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

        try {
            com.caffein.studentservice.grpc.CourseResponse courseResponse = courseInformationServiceBlockingStub.getCourseById(
                    com.caffein.studentservice.grpc.CourseRequest.newBuilder().setCourseId(request.getCourseId().toString()).build()
            );

            long currentEnrollmentCount = enrollmentRepository.countByCourseId(request.getCourseId());
            if (currentEnrollmentCount >= courseResponse.getMaxCapacity()) {
                throw new IllegalStateException("Course has reached its maximum capacity");
            }

            String courseCode = courseResponse.hasSubject() ? courseResponse.getSubject().getName() : "N/A";

            Enrollment enrollment = Enrollment.builder()
                    .student(student)
                    .courseId(request.getCourseId())
                    .courseCode(courseCode)
                    .courseName(courseResponse.getName())
                    .academicYear(courseResponse.getAcademicYear())
                    .semester(courseResponse.getSemester())
                    .status(Enrollment.EnrollmentStatus.REGISTERED)
                    .build();

            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            log.info("Successfully enrolled student {} in course {}", student.getId(), request.getCourseId());

            return enrollmentMapper.toDTO(savedEnrollment);
        } catch (StatusRuntimeException e) {
            log.error("gRPC error while fetching course info for courseId {}: {}", request.getCourseId(), e.getStatus());
            throw new IllegalStateException("Failed to retrieve course information. Please try again later.", e);
        }
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

    @Override
    public List<SubjectDTO> getAllSubjects() {
        log.info("Fetching all subjects from course service");
        try {
            Iterator<com.caffein.studentservice.grpc.SubjectResponse> responseIterator =
                    courseInformationServiceBlockingStub.getSubjects(com.caffein.studentservice.grpc.SubjectsRequest.newBuilder().build());

            List<SubjectDTO> subjects = new ArrayList<>();
            while (responseIterator.hasNext()) {
                com.caffein.studentservice.grpc.SubjectResponse subjectResponse = responseIterator.next();
                subjects.add(SubjectDTO.builder()
                        .id(UUID.fromString(subjectResponse.getId()))
                        .name(subjectResponse.getName())
                        .department(subjectResponse.getDepartment())
                        .build());
            }
            return subjects;
        } catch (StatusRuntimeException e) {
            log.error("gRPC error while fetching subjects: {}", e.getStatus());
            throw new IllegalStateException("Failed to retrieve subjects. Please try again later.", e);
        }
    }
}