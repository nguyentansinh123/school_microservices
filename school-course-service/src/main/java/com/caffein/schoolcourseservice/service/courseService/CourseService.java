package com.caffein.schoolcourseservice.service.courseService;

import com.caffein.schoolcourseservice.dto.course.CourseCreateDTO;
import com.caffein.schoolcourseservice.dto.course.CourseDTO;
import com.caffein.schoolcourseservice.dto.course.CourseUpdateDTO;
import com.caffein.schoolcourseservice.dto.course.mapper.CourseMapper;
import com.caffein.schoolcourseservice.model.Course;
import com.caffein.schoolcourseservice.model.Subject;
import com.caffein.schoolcourseservice.model.Teacher;
import com.caffein.schoolcourseservice.repository.CourseRepository;
import com.caffein.schoolcourseservice.repository.SubjectRepository;
import com.caffein.schoolcourseservice.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService implements ICourseService{

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseDTO createCourse(CourseCreateDTO createDTO) {
        // Validate subject exists
        Subject subject = subjectRepository.findById(createDTO.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Subject not found with ID: " + createDTO.getSubjectId()));

        // Validate teacher exists
        Teacher teacher = teacherRepository.findById(createDTO.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + createDTO.getTeacherId()));

        // Check if teacher is qualified for this subject
        if (!teacher.getSubjects().contains(subject)) {
            throw new IllegalArgumentException("Teacher is not qualified to teach this subject");
        }

        Course course = Course.builder()
                .subject(subject)
                .teacher(teacher)
                .name(createDTO.getName())
                .description(createDTO.getDescription())
                .academicYear(createDTO.getAcademicYear())
                .semester(createDTO.getSemester())
                .maxCapacity(createDTO.getMaxCapacity())
                .build();

        Course savedCourse = courseRepository.save(course);
        log.info("Created new course: {} for subject: {} with teacher: {}",
                savedCourse.getName(), subject.getName(), teacher.getEmail());

        return courseMapper.toDTO(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses(String subject, String teacher, String academicYear, String semester) {
        List<Course> courses;

        if (hasAnyFilter(subject, teacher, academicYear, semester)) {
            courses = courseRepository.findCoursesWithFilters(subject, teacher, academicYear, semester);
        } else {
            courses = courseRepository.findAll();
        }

        return courses.stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(UUID id) {
        Course course = courseRepository.findByIdWithSchedules(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));
        return courseMapper.toDTO(course);
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(UUID id, CourseUpdateDTO updateDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));

        // Update teacher if provided
        if (updateDTO.getTeacherId() != null) {
            Teacher newTeacher = teacherRepository.findById(updateDTO.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found with ID: " + updateDTO.getTeacherId()));

            // Check if new teacher is qualified for this subject
            if (!newTeacher.getSubjects().contains(course.getSubject())) {
                throw new IllegalArgumentException("New teacher is not qualified to teach this subject");
            }

            course.setTeacher(newTeacher);
        }

        // Update other fields
        if (updateDTO.getName() != null) {
            course.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            course.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getAcademicYear() != null) {
            course.setAcademicYear(updateDTO.getAcademicYear());
        }
        if (updateDTO.getSemester() != null) {
            course.setSemester(updateDTO.getSemester());
        }
        if (updateDTO.getMaxCapacity() != null) {
            course.setMaxCapacity(updateDTO.getMaxCapacity());
        }

        Course updatedCourse = courseRepository.save(course);
        log.info("Updated course: {}", updatedCourse.getName());

        return courseMapper.toDTO(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));

        courseRepository.delete(course);
        log.info("Deleted course: {}", course.getName());
    }

    private boolean hasAnyFilter(String subject, String teacher, String academicYear, String semester) {
        return (subject != null && !subject.trim().isEmpty()) ||
                (teacher != null && !teacher.trim().isEmpty()) ||
                (academicYear != null && !academicYear.trim().isEmpty()) ||
                (semester != null && !semester.trim().isEmpty());
    }
}
