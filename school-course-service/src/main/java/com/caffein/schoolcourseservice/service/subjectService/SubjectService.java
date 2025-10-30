package com.caffein.schoolcourseservice.service.subjectService;

import com.caffein.schoolcourseservice.dto.subject.SubjectCreateDTO;
import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;
import com.caffein.schoolcourseservice.dto.subject.mapper.SubjectMapper;
import com.caffein.schoolcourseservice.model.Subject;
import com.caffein.schoolcourseservice.repository.SubjectRepository;
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
public class SubjectService implements  ISubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    @Transactional
    public SubjectDTO createSubject(SubjectCreateDTO createDTO) {
        // Check if subject already exists
        if (subjectRepository.existsByName(createDTO.getName())) {
            throw new IllegalArgumentException("Subject with name '" + createDTO.getName() + "' already exists");
        }

        Subject subject = Subject.builder()
                .name(createDTO.getName())
                .department(createDTO.getDepartment())
                .build();

        Subject savedSubject = subjectRepository.save(subject);
        log.info("Created new subject: {} in {}", savedSubject.getName(), savedSubject.getDepartment());

        return subjectMapper.toDTO(savedSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(subjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectDTO getSubjectById(UUID id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with ID: " + id));
        return subjectMapper.toDTO(subject);
    }

    @Override
    @Transactional
    public void deleteSubject(UUID id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with ID: " + id));

        // Check if subject has associated courses
        if (!subject.getCourses().isEmpty()) {
            throw new IllegalStateException("Cannot delete subject with existing courses. Please remove all courses first.");
        }

        // Remove associations with teachers
        subject.getTeachers().forEach(teacher -> teacher.getSubjects().remove(subject));
        subject.getTeachers().clear();

        subjectRepository.delete(subject);
        log.info("Deleted subject: {}", subject.getName());
    }
}
