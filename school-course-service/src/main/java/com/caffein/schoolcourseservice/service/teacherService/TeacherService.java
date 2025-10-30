package com.caffein.schoolcourseservice.service.teacherService;

import com.caffein.schoolcourseservice.dto.teacher.TeacherDTO;
import com.caffein.schoolcourseservice.dto.teacher.TeacherUpdateDTO;
import com.caffein.schoolcourseservice.dto.teacher.mapper.TeacherMapper;
import com.caffein.schoolcourseservice.dto.user.UserDTO;
import com.caffein.schoolcourseservice.model.Subject;
import com.caffein.schoolcourseservice.model.Teacher;
import com.caffein.schoolcourseservice.repository.SubjectRepository;
import com.caffein.schoolcourseservice.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherMapper teacherMapper;

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

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDTO> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream().map(teacherMapper::toDTO).collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDTO getTeacherByUserId(UUID userId) {
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found for user ID: " + userId));
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional
    public TeacherDTO updateTeacher(UUID id, TeacherUpdateDTO updateDTO) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + id));

        // Update fields
        if (updateDTO.getDepartment() != null) {
            teacher.setDepartment(updateDTO.getDepartment());
        }
        if (updateDTO.getOfficeLocation() != null) {
            teacher.setOfficeLocation(updateDTO.getOfficeLocation());
        }
        if (updateDTO.getHireDate() != null) {
            teacher.setHireDate(updateDTO.getHireDate());
        }
        if (updateDTO.getPhoneNumber() != null) {
            teacher.setPhoneNumber(updateDTO.getPhoneNumber());
        }

        Teacher updatedTeacher = teacherRepository.save(teacher);
        log.info("Updated teacher profile for ID: {}", id);
        return teacherMapper.toDTO(updatedTeacher);
    }

    @Override
    @Transactional
    public void assignSubjectToTeacher(UUID teacherId, UUID subjectId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + teacherId));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with ID: " + subjectId));

        // Check if already assigned
        if (teacher.getSubjects().contains(subject)) {
            log.warn("Subject {} already assigned to teacher {}", subject.getName(), teacher.getEmail());
            return;
        }

        teacher.getSubjects().add(subject);
        subject.getTeachers().add(teacher);

        teacherRepository.save(teacher);
        subjectRepository.save(subject);

        log.info("Assigned subject {} to teacher {}", subject.getName(), teacher.getEmail());
    }

    @Override
    @Transactional
    public void removeSubjectFromTeacher(UUID teacherId, UUID subjectId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + teacherId));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with ID: " + subjectId));

        teacher.getSubjects().remove(subject);
        subject.getTeachers().remove(teacher);

        teacherRepository.save(teacher);
        subjectRepository.save(subject);

        log.info("Removed subject {} from teacher {}", subject.getName(), teacher.getEmail());
    }
}
