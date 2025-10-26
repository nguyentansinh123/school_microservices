package com.caffein.studentservice.service.guardianService;

import com.caffein.studentservice.dto.GuardianDTO;

import java.util.List;
import java.util.UUID;

public interface IGuardianService {
    GuardianDTO addGuardianToStudent(UUID studentId, GuardianDTO guardianDTO);
    List<GuardianDTO> getGuardiansByStudentId(UUID studentId);
}