package com.caffein.schoolcourseservice;

import com.caffein.schoolcourseservice.model.*;
import com.caffein.schoolcourseservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.UUID;

@SpringBootApplication
public class SchoolCourseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolCourseServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            SubjectRepository subjectRepository,
            TeacherRepository teacherRepository,
            CourseRepository courseRepository,
            ScheduleRepository scheduleRepository,
            DataInitializer dataInitializer) {
        return args -> {
            dataInitializer.initializeData(subjectRepository, teacherRepository, courseRepository, scheduleRepository);
        };
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