package com.caffein.schoolcourseservice.service.teacherService;

import com.caffein.schoolcourseservice.dto.UserDTO;
import com.caffein.schoolcourseservice.model.Teacher;
import com.caffein.schoolcourseservice.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public Teacher createTeacherFromUser(UserDTO userDTO) {
        try {
            UUID userId = UUID.fromString(userDTO.getId());

            // Check if teacher already exists
            if (teacherRepository.existsByUserId(userId)) {
                log.info("Teacher already exists for user ID: {}, skipping creation", userId);
                return teacherRepository.findByUserId(userId).orElse(null);
            }

            // Build teacher from UserDTO
            Teacher teacher = Teacher.builder()
                    .userId(userId)
                    .firstName(userDTO.getFirstName())
                    .lastName(userDTO.getLastName())
                    .email(userDTO.getEmail())
                    .phoneNumber(userDTO.getPhoneNumber())
                    .subjects(new HashSet<>()) 
                    .build();

            Teacher savedTeacher = teacherRepository.save(teacher);
            log.info("Successfully created teacher record for user: {} {} with ID: {}",
                    userDTO.getFirstName(), userDTO.getLastName(), savedTeacher.getId());

            return savedTeacher;
        } catch (Exception e) {
            log.error("Error creating teacher record for user: {}", userDTO.getEmail(), e);
            throw new RuntimeException("Failed to create teacher from user data", e);
        }
    }
}
