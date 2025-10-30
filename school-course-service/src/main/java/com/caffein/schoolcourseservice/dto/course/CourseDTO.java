package com.caffein.schoolcourseservice.dto.course;

import com.caffein.schoolcourseservice.dto.schedule.ScheduleDTO;
import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;
import com.caffein.schoolcourseservice.dto.teacher.TeacherDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private UUID id;
    private SubjectDTO subject;
    private TeacherDTO teacher;
    private String name;
    private String description;
    private String academicYear;
    private String semester;
    private Integer maxCapacity;
    private List<ScheduleDTO> schedules;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}