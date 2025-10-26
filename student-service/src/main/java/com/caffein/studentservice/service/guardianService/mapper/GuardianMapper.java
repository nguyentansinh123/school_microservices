package com.caffein.studentservice.service.guardianService.mapper;

import com.caffein.studentservice.dto.GuardianDTO;
import com.caffein.studentservice.model.Guardian;
import org.springframework.stereotype.Component;

@Component
public class GuardianMapper {

    public GuardianDTO toDTO(Guardian guardian) {
        return GuardianDTO.builder()
                .id(guardian.getId())
                .firstName(guardian.getFirstName())
                .lastName(guardian.getLastName())
                .phoneNumber(guardian.getPhoneNumber())
                .email(guardian.getEmail())
                .relationship(guardian.getRelationship())
                .studentId(guardian.getStudent() != null ? guardian.getStudent().getId() : null)
                .build();
    }

    public Guardian toEntity(GuardianDTO guardianDTO) {
        return Guardian.builder()
                .firstName(guardianDTO.getFirstName())
                .lastName(guardianDTO.getLastName())
                .phoneNumber(guardianDTO.getPhoneNumber())
                .email(guardianDTO.getEmail())
                .relationship(guardianDTO.getRelationship())
                .build();
    }
}