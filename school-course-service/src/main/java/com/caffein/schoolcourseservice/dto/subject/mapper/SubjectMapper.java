package com.caffein.schoolcourseservice.dto.subject.mapper;

import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;
import com.caffein.schoolcourseservice.model.Subject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SubjectMapper {

    public SubjectDTO toDTO(Subject subject) {
        if (subject == null) return null;

        return SubjectDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .department(subject.getDepartment())
                .teachers(new ArrayList<>()) // Will be populated by TeacherMapper if needed
                .courses(new ArrayList<>())  // Will be populated by CourseMapper if needed
                .build();
    }

    public SubjectDTO toDTOWithoutRelations(Subject subject) {
        if (subject == null) return null;

        return SubjectDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .department(subject.getDepartment())
                .build();
    }
}