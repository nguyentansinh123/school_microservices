package com.caffein.studentservice.repository;

import com.caffein.studentservice.model.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GuardianRepository extends JpaRepository<Guardian, UUID> {
}
