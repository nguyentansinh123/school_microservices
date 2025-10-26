package com.caffein.studentservice.service.studentService;

import com.caffein.studentservice.dto.StudentDTO;
import com.caffein.studentservice.dto.StudentUpdateDTO;
import com.caffein.studentservice.dto.UserDTO;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.repository.StudentRepository;
import com.caffein.studentservice.service.studentService.mapper.StudentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService implements IStudentService{

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public Student createStudentFromUser(UserDTO userDTO) {
        try {
            UUID userId = UUID.fromString(userDTO.getId());

            // Check if student already exists
            if (studentRepository.existsByUserId(userId)) {
                log.info("Student already exists for user ID: {}, skipping creation", userId);
                return studentRepository.findByUserId(userId).orElse(null);
            }

            Student student = Student.builder()
                    .userId(userId)
                    .firstName(userDTO.getFirstName())
                    .lastName(userDTO.getLastName())
                    .email(userDTO.getEmail())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .dateOfBirth(userDTO.getDateOfBirth())
                    .enrollmentStatus(Student.EnrollmentStatus.PENDING)
                    .build();

            Student savedStudent = studentRepository.save(student);
            log.info("Successfully created student record for user: {} with ID: {}",
                    userDTO.getEmail(), savedStudent.getId());

            return savedStudent;
        } catch (Exception e) {
            log.error("Error creating student record for user: {}", userDTO.getEmail(), e);
            throw e;
        }
    }

    @Override
    public Page<StudentDTO> getAllStudents(String search, Pageable pageable) {
        Page<Student> students;

        if (search != null && !search.trim().isEmpty()) {
            students = studentRepository.findBySearchCriteria(search.trim(), pageable);
        } else {
            students = studentRepository.findAll(pageable);
        }

        return students.map(studentMapper::toDTO);
    }

    @Override
    public StudentDTO getStudentById(UUID id) {
        Student student = studentRepository.findByIdWithGuardians(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));

        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional
    public StudentDTO updateStudent(UUID id, StudentUpdateDTO updateDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));

        // Update student fields
        studentMapper.updateStudentFromDTO(student, updateDTO);

        // Activate enrollment if requested
        if (updateDTO.isActivateEnrollment() && student.getEnrollmentStatus() == Student.EnrollmentStatus.PENDING) {
            student.setEnrollmentStatus(Student.EnrollmentStatus.ACTIVE);
            log.info("Activated enrollment for student ID: {}", id);
        }

        Student savedStudent = studentRepository.save(student);
        log.info("Updated student profile for ID: {}", id);

        return studentMapper.toDTO(savedStudent);
    }
}
