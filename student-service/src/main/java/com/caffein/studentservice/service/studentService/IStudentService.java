package com.caffein.studentservice.service.studentService;

import com.caffein.studentservice.dto.UserDTO;
import com.caffein.studentservice.model.Student;

public interface IStudentService {

    public Student createStudentFromUser(UserDTO userDTO);
}
