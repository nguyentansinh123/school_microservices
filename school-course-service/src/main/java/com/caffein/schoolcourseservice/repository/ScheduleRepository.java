package com.caffein.schoolcourseservice.repository;

import com.caffein.schoolcourseservice.model.DayOfWeek;
import com.caffein.schoolcourseservice.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByCourseId(UUID courseId);
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<Schedule> findByCourseIdAndDayOfWeek(UUID courseId, DayOfWeek dayOfWeek);
}