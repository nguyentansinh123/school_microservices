package com.caffein.schoolcourseservice.repository;

import com.caffein.schoolcourseservice.model.DayOfWeek;
import com.caffein.schoolcourseservice.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findBySubjectId(UUID subjectId);
    List<Schedule> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<Schedule> findBySubjectIdAndDayOfWeek(UUID subjectId, DayOfWeek dayOfWeek);
    List<Schedule> findByAcademicYearAndSemester(String academicYear, String semester);
    List<Schedule> findBySubjectIdAndAcademicYearAndSemester(UUID subjectId, String academicYear, String semester);
}