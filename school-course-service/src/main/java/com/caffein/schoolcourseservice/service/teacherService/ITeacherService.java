package com.caffein.schoolcourseservice.service.teacherService;

import com.caffein.schoolcourseservice.dto.teacher.TeacherDTO;
import com.caffein.schoolcourseservice.dto.teacher.TeacherUpdateDTO;
import com.caffein.schoolcourseservice.dto.user.UserDTO;
import com.caffein.schoolcourseservice.model.Teacher;

import java.util.List;
import java.util.UUID;

public interface ITeacherService {
    Teacher createTeacherFromUser(UserDTO userDTO);
    List<TeacherDTO> getAllTeachers();
    TeacherDTO getTeacherByUserId(UUID userId);
    TeacherDTO updateTeacher(UUID id, TeacherUpdateDTO updateDTO);
    void assignSubjectToTeacher(UUID teacherId, UUID subjectId);
    void removeSubjectFromTeacher(UUID teacherId, UUID subjectId);
}
