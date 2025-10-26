package com.caffein.studentservice.service.guardianService;

import com.caffein.studentservice.dto.GuardianDTO;
import com.caffein.studentservice.model.Guardian;
import com.caffein.studentservice.model.Student;
import com.caffein.studentservice.repository.GuardianRepository;
import com.caffein.studentservice.repository.StudentRepository;
import com.caffein.studentservice.service.guardianService.mapper.GuardianMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuardianService implements IGuardianService {

    private final GuardianRepository guardianRepository;
    private final StudentRepository studentRepository;
    private final GuardianMapper guardianMapper;

    @Override
    @Transactional
    public GuardianDTO addGuardianToStudent(UUID studentId, GuardianDTO guardianDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

        Guardian guardian = guardianMapper.toEntity(guardianDTO);
        guardian.setStudent(student);

        Guardian savedGuardian = guardianRepository.save(guardian);
        log.info("Added guardian {} {} to student ID: {}",
                guardian.getFirstName(), guardian.getLastName(), studentId);

        return guardianMapper.toDTO(savedGuardian);
    }

    @Override
    public List<GuardianDTO> getGuardiansByStudentId(UUID studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not found with ID: " + studentId);
        }

        List<Guardian> guardians = guardianRepository.findByStudentId(studentId);
        return guardians.stream()
                .map(guardianMapper::toDTO)
                .toList();
    }
}