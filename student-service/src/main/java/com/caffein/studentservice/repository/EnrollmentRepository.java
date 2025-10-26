package com.caffein.studentservice.repository;

import com.caffein.studentservice.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
}
