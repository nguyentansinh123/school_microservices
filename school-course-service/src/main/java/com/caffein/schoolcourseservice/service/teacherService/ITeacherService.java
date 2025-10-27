package com.caffein.schoolcourseservice.service.teacherService;

import com.caffein.schoolcourseservice.dto.UserDTO;
import com.caffein.schoolcourseservice.model.Teacher;

public interface ITeacherService {
    Teacher createTeacherFromUser(UserDTO userDTO);
}
