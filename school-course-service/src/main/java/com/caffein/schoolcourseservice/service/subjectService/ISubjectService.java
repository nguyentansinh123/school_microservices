package com.caffein.schoolcourseservice.service.subjectService;


import com.caffein.schoolcourseservice.dto.subject.SubjectCreateDTO;
import com.caffein.schoolcourseservice.dto.subject.SubjectDTO;

import java.util.List;
import java.util.UUID;

public interface ISubjectService {
    SubjectDTO createSubject(SubjectCreateDTO createDTO);
    List<SubjectDTO> getAllSubjects();
    SubjectDTO getSubjectById(UUID id);
    void deleteSubject(UUID id);
}