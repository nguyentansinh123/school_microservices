package com.caffein.schoolcourseservice.dto.subject;

import com.caffein.schoolcourseservice.dto.course.CourseDTO;
import com.caffein.schoolcourseservice.dto.teacher.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDTO {
    private UUID id;
    private String name;
    private String department;
    private List<TeacherDTO> teachers;
    private List<CourseDTO> courses;
}