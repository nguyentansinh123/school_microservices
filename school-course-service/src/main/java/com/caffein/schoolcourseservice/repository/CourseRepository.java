package com.caffein.schoolcourseservice.repository;

import com.caffein.schoolcourseservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByTeacherId(UUID teacherId);
    List<Course> findBySubjectId(UUID subjectId);
    List<Course> findByAcademicYearAndSemester(String academicYear, String semester);
}