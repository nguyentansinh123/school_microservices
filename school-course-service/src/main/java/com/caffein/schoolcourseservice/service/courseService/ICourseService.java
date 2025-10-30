package com.caffein.schoolcourseservice.service.courseService;

import com.caffein.schoolcourseservice.dto.course.CourseCreateDTO;
import com.caffein.schoolcourseservice.dto.course.CourseDTO;
import com.caffein.schoolcourseservice.dto.course.CourseUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface ICourseService {
    CourseDTO createCourse(CourseCreateDTO createDTO);
    List<CourseDTO> getAllCourses(String subject, String teacher, String academicYear, String semester);
    CourseDTO getCourseById(UUID id);
    CourseDTO updateCourse(UUID id, CourseUpdateDTO updateDTO);
    void deleteCourse(UUID id);
}