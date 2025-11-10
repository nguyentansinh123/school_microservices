package com.caffein.schoolcourseservice.repository;

import com.caffein.schoolcourseservice.model.Course;
import com.caffein.schoolcourseservice.model.Subject;
import com.caffein.schoolcourseservice.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByTeacherId(UUID teacherId);
    List<Course> findBySubjectId(UUID subjectId);
    List<Course> findByAcademicYearAndSemester(String academicYear, String semester);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.schedules WHERE c.id = :id")
    Optional<Course> findByIdWithSchedules(@Param("id") UUID id);

    @Query("SELECT DISTINCT c FROM Course c " +
            "LEFT JOIN c.subject s " +
            "LEFT JOIN c.teacher t " +
            "WHERE (:subject IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :subject, '%'))) " +
            "AND (:teacher IS NULL OR LOWER(CONCAT(t.firstName, ' ', t.lastName)) LIKE LOWER(CONCAT('%', :teacher, '%'))) " +
            "AND (:academicYear IS NULL OR c.academicYear = :academicYear) " +
            "AND (:semester IS NULL OR c.semester = :semester)")
    List<Course> findCoursesWithFilters(
            @Param("subject") String subject,
            @Param("teacher") String teacher,
            @Param("academicYear") String academicYear,
            @Param("semester") String semester
    );

    Optional<Course> findByNameAndSubjectAndTeacher(String name, Subject subject, Teacher teacher);
}