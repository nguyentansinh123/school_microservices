package com.caffein.schoolcourseservice;

import com.caffein.schoolcourseservice.model.*;
import com.caffein.schoolcourseservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    @Transactional
    public void initializeData(SubjectRepository subjectRepository,
                               TeacherRepository teacherRepository,
                               CourseRepository courseRepository,
                               ScheduleRepository scheduleRepository) {
        // Create subjects
        createSubjectIfNotExists(subjectRepository, "Mathematics", "Mathematics Department");
        createSubjectIfNotExists(subjectRepository, "English Literature", "English Department");
        createSubjectIfNotExists(subjectRepository, "Physics", "Science Department");
        createSubjectIfNotExists(subjectRepository, "Chemistry", "Science Department");
        createSubjectIfNotExists(subjectRepository, "Biology", "Science Department");
        createSubjectIfNotExists(subjectRepository, "History", "Social Studies Department");
        createSubjectIfNotExists(subjectRepository, "Geography", "Social Studies Department");
        createSubjectIfNotExists(subjectRepository, "Economics", "Social Studies Department");
        createSubjectIfNotExists(subjectRepository, "French", "Foreign Languages Department");
        createSubjectIfNotExists(subjectRepository, "Spanish", "Foreign Languages Department");
        createSubjectIfNotExists(subjectRepository, "German", "Foreign Languages Department");
        createSubjectIfNotExists(subjectRepository, "Art and Design", "Arts Department");
        createSubjectIfNotExists(subjectRepository, "Music", "Arts Department");
        createSubjectIfNotExists(subjectRepository, "Drama and Theatre", "Arts Department");
        createSubjectIfNotExists(subjectRepository, "Computer Science", "Technology Department");
        createSubjectIfNotExists(subjectRepository, "Information Technology", "Technology Department");
        createSubjectIfNotExists(subjectRepository, "Web Development", "Technology Department");
        createSubjectIfNotExists(subjectRepository, "Data Science", "Technology Department");
        createSubjectIfNotExists(subjectRepository, "Business Studies", "Business Department");
        createSubjectIfNotExists(subjectRepository, "Accounting", "Business Department");
        createSubjectIfNotExists(subjectRepository, "Physical Education", "Health and PE Department");
        createSubjectIfNotExists(subjectRepository, "Health Education", "Health and PE Department");
        createSubjectIfNotExists(subjectRepository, "Calculus", "Mathematics Department");
        createSubjectIfNotExists(subjectRepository, "Statistics", "Mathematics Department");
        createSubjectIfNotExists(subjectRepository, "Environmental Science", "Science Department");
        createSubjectIfNotExists(subjectRepository, "Philosophy", "Humanities Department");
        createSubjectIfNotExists(subjectRepository, "Psychology", "Humanities Department");
        createSubjectIfNotExists(subjectRepository, "Sociology", "Humanities Department");
        createSubjectIfNotExists(subjectRepository, "Engineering", "Technical Department");
        createSubjectIfNotExists(subjectRepository, "Architecture", "Technical Department");

        System.out.println("✓ Created " + subjectRepository.count() + " subjects");

        // Create Teacher 1
        Subject math = subjectRepository.findByName("Mathematics").orElse(null);
        Subject calculus = subjectRepository.findByName("Calculus").orElse(null);

        Teacher teacher1 = Teacher.builder()
                .userId(UUID.randomUUID())
                .firstName("Sarah")
                .lastName("Johnson")
                .email("sarah.johnson@school.edu")
                .phoneNumber("+1234567890")
                .department("Mathematics Department")
                .officeLocation("Building A, Room 201")
                .hireDate(LocalDate.of(2015, 8, 15))
                .subjects(new HashSet<>())
                .build();

        // Add subjects directly to teacher without accessing subject.teachers
        if (math != null) teacher1.getSubjects().add(math);
        if (calculus != null) teacher1.getSubjects().add(calculus);

        teacher1 = teacherRepository.save(teacher1);
        System.out.println("✓ Created teacher: Sarah Johnson");

        // Create Teacher 2
        Subject cs = subjectRepository.findByName("Computer Science").orElse(null);
        Subject web = subjectRepository.findByName("Web Development").orElse(null);

        Teacher teacher2 = Teacher.builder()
                .userId(UUID.randomUUID())
                .firstName("Michael")
                .lastName("Chen")
                .email("michael.chen@school.edu")
                .phoneNumber("+1234567891")
                .department("Technology Department")
                .officeLocation("Building B, Room 305")
                .hireDate(LocalDate.of(2018, 1, 10))
                .subjects(new HashSet<>())
                .build();

        if (cs != null) teacher2.getSubjects().add(cs);
        if (web != null) teacher2.getSubjects().add(web);

        teacher2 = teacherRepository.save(teacher2);
        System.out.println("✓ Created teacher: Michael Chen");

        // Create Course 1
        if (calculus != null && teacher1 != null) {
            Course course1 = Course.builder()
                    .subject(calculus)
                    .teacher(teacher1)
                    .name("Calculus I - Fall 2024")
                    .description("Introduction to differential and integral calculus")
                    .academicYear("2024-2025")
                    .semester("Fall")
                    .maxCapacity(30)
                    .build();

            course1 = courseRepository.save(course1);
            System.out.println("✓ Created course: Calculus I");

            // Add schedules for Course 1
            Schedule schedule1 = Schedule.builder()
                    .course(course1)
                    .dayOfWeek(DayOfWeek.MONDAY)
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(10, 30))
                    .roomNumber("Room 101")
                    .build();
            scheduleRepository.save(schedule1);

            Schedule schedule2 = Schedule.builder()
                    .course(course1)
                    .dayOfWeek(DayOfWeek.WEDNESDAY)
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(10, 30))
                    .roomNumber("Room 101")
                    .build();
            scheduleRepository.save(schedule2);

            System.out.println("  ✓ Added schedules: MON & WED 9:00-10:30");
        }

        // Create Course 2
        if (cs != null && teacher2 != null) {
            Course course2 = Course.builder()
                    .subject(cs)
                    .teacher(teacher2)
                    .name("Introduction to Programming")
                    .description("Learn Java programming fundamentals")
                    .academicYear("2024-2025")
                    .semester("Fall")
                    .maxCapacity(40)
                    .build();

            course2 = courseRepository.save(course2);
            System.out.println("✓ Created course: Introduction to Programming");

            // Add schedules for Course 2
            Schedule schedule3 = Schedule.builder()
                    .course(course2)
                    .dayOfWeek(DayOfWeek.TUESDAY)
                    .startTime(LocalTime.of(14, 0))
                    .endTime(LocalTime.of(15, 30))
                    .roomNumber("Lab 201")
                    .build();
            scheduleRepository.save(schedule3);

            Schedule schedule4 = Schedule.builder()
                    .course(course2)
                    .dayOfWeek(DayOfWeek.THURSDAY)
                    .startTime(LocalTime.of(14, 0))
                    .endTime(LocalTime.of(15, 30))
                    .roomNumber("Lab 201")
                    .build();
            scheduleRepository.save(schedule4);

            System.out.println("  ✓ Added schedules: TUE & THU 14:00-15:30");
        }

        System.out.println("\n=== MOCK DATA CREATION COMPLETE ===");
        System.out.println("Teachers: " + teacherRepository.count());
        System.out.println("Courses: " + courseRepository.count());
        System.out.println("Schedules: " + scheduleRepository.count());
    }

    private void createSubjectIfNotExists(SubjectRepository subjectRepository, String name, String department) {
        if (!subjectRepository.existsByName(name)) {
            Subject subject = Subject.builder()
                    .name(name)
                    .department(department)
                    .build();
            subjectRepository.save(subject);
        }
    }
}