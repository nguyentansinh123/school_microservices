package com.caffein.studentservice.service.studentService;

import com.caffein.studentservice.dto.UserDTO;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService implements IStudentService{

    private final StudentRepository studentRepository;

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
}