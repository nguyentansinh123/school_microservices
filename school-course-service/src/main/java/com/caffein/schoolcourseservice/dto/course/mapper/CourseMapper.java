package com.caffein.schoolcourseservice.dto.course.mapper;

import com.caffein.schoolcourseservice.dto.course.CourseDTO;
import com.caffein.schoolcourseservice.dto.schedule.mapper.ScheduleMapper;
import com.caffein.schoolcourseservice.dto.subject.mapper.SubjectMapper;
import com.caffein.schoolcourseservice.dto.teacher.mapper.TeacherMapper;
import com.caffein.schoolcourseservice.model.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final SubjectMapper subjectMapper;
    private final TeacherMapper teacherMapper;
    private final ScheduleMapper scheduleMapper;

    public CourseDTO toDTO(Course course) {
        if (course == null) return null;

        return CourseDTO.builder()
                .id(course.getId())
                .subject(subjectMapper.toDTOWithoutRelations(course.getSubject()))
                .teacher(teacherMapper.toDTOWithoutRelations(course.getTeacher()))
                .name(course.getName())
                .description(course.getDescription())
                .academicYear(course.getAcademicYear())
                .semester(course.getSemester())
                .maxCapacity(course.getMaxCapacity())
                .schedules(course.getSchedules() != null ?
                        course.getSchedules().stream()
                                .map(scheduleMapper::toDTO)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }

    public CourseDTO toDTOWithoutSchedules(Course course) {
        if (course == null) return null;

        return CourseDTO.builder()
                .id(course.getId())
                .subject(subjectMapper.toDTOWithoutRelations(course.getSubject()))
                .teacher(teacherMapper.toDTOWithoutRelations(course.getTeacher()))
                .name(course.getName())
                .description(course.getDescription())
                .academicYear(course.getAcademicYear())
                .semester(course.getSemester())
                .maxCapacity(course.getMaxCapacity())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}