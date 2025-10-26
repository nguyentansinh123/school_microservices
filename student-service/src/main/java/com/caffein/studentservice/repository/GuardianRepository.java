package com.caffein.studentservice.repository;

import com.caffein.studentservice.model.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GuardianRepository extends JpaRepository<Guardian, UUID> {
    @Query("SELECT g FROM Guardian g WHERE g.student.id = :studentId")
    List<Guardian> findByStudentId(@Param("studentId") UUID studentId);
}
