package com.caffein.studentservice.service.studentService;

import com.caffein.studentservice.dto.StudentDTO;
import com.caffein.studentservice.dto.StudentUpdateDTO;
import com.caffein.studentservice.dto.UserDTO;
import com.caffein.studentservice.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IStudentService {

    public Student createStudentFromUser(UserDTO userDTO);

    Page<StudentDTO> getAllStudents(String search, Pageable pageable);

    StudentDTO getStudentById(UUID id);

    StudentDTO updateStudent(UUID id, StudentUpdateDTO updateDTO);
}
