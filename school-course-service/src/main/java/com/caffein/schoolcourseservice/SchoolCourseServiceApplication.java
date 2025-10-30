package com.caffein.schoolcourseservice;

import com.caffein.schoolcourseservice.model.Subject;
import com.caffein.schoolcourseservice.repository.ScheduleRepository;
import com.caffein.schoolcourseservice.repository.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolCourseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolCourseServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(SubjectRepository subjectRepository, ScheduleRepository scheduleRepository) {
        return args -> {
            // Create core academic subjects
            createSubjectIfNotExists(subjectRepository, "Mathematics", "Mathematics Department");
            createSubjectIfNotExists(subjectRepository, "English Literature", "English Department");
            createSubjectIfNotExists(subjectRepository, "English Language", "English Department");
            createSubjectIfNotExists(subjectRepository, "Physics", "Science Department");
            createSubjectIfNotExists(subjectRepository, "Chemistry", "Science Department");
            createSubjectIfNotExists(subjectRepository, "Biology", "Science Department");
            createSubjectIfNotExists(subjectRepository, "History", "Social Studies Department");
            createSubjectIfNotExists(subjectRepository, "Geography", "Social Studies Department");
            createSubjectIfNotExists(subjectRepository, "Political Science", "Social Studies Department");
            createSubjectIfNotExists(subjectRepository, "Economics", "Social Studies Department");

            // Language subjects
            createSubjectIfNotExists(subjectRepository, "French", "Foreign Languages Department");
            createSubjectIfNotExists(subjectRepository, "Spanish", "Foreign Languages Department");
            createSubjectIfNotExists(subjectRepository, "German", "Foreign Languages Department");
            createSubjectIfNotExists(subjectRepository, "Italian", "Foreign Languages Department");
            createSubjectIfNotExists(subjectRepository, "Arabic", "Foreign Languages Department");
            createSubjectIfNotExists(subjectRepository, "Chinese (Mandarin)", "Foreign Languages Department");

            // Arts subjects
            createSubjectIfNotExists(subjectRepository, "Art and Design", "Arts Department");
            createSubjectIfNotExists(subjectRepository, "Music", "Arts Department");
            createSubjectIfNotExists(subjectRepository, "Drama and Theatre", "Arts Department");
            createSubjectIfNotExists(subjectRepository, "Photography", "Arts Department");

            // Technology and Computer Science
            createSubjectIfNotExists(subjectRepository, "Computer Science", "Technology Department");
            createSubjectIfNotExists(subjectRepository, "Information Technology", "Technology Department");
            createSubjectIfNotExists(subjectRepository, "Digital Media", "Technology Department");
            createSubjectIfNotExists(subjectRepository, "Web Development", "Technology Department");
            createSubjectIfNotExists(subjectRepository, "Data Science", "Technology Department");
            createSubjectIfNotExists(subjectRepository, "Software Engineering", "Technology Department");

            // Business and Economics
            createSubjectIfNotExists(subjectRepository, "Business Studies", "Business Department");
            createSubjectIfNotExists(subjectRepository, "Accounting", "Business Department");
            createSubjectIfNotExists(subjectRepository, "Marketing", "Business Department");
            createSubjectIfNotExists(subjectRepository, "Entrepreneurship", "Business Department");

            // Physical Education and Health
            createSubjectIfNotExists(subjectRepository, "Physical Education", "Health and PE Department");
            createSubjectIfNotExists(subjectRepository, "Health Education", "Health and PE Department");
            createSubjectIfNotExists(subjectRepository, "Sports Science", "Health and PE Department");

            // Advanced Mathematics and Sciences
            createSubjectIfNotExists(subjectRepository, "Calculus", "Mathematics Department");
            createSubjectIfNotExists(subjectRepository, "Statistics", "Mathematics Department");
            createSubjectIfNotExists(subjectRepository, "Advanced Mathematics", "Mathematics Department");
            createSubjectIfNotExists(subjectRepository, "Environmental Science", "Science Department");
            createSubjectIfNotExists(subjectRepository, "Astronomy", "Science Department");
            createSubjectIfNotExists(subjectRepository, "Geology", "Science Department");

            // Humanities
            createSubjectIfNotExists(subjectRepository, "Philosophy", "Humanities Department");
            createSubjectIfNotExists(subjectRepository, "Psychology", "Humanities Department");
            createSubjectIfNotExists(subjectRepository, "Sociology", "Humanities Department");
            createSubjectIfNotExists(subjectRepository, "Anthropology", "Humanities Department");
            createSubjectIfNotExists(subjectRepository, "Religious Studies", "Humanities Department");

            // Vocational and Technical subjects
            createSubjectIfNotExists(subjectRepository, "Engineering", "Technical Department");
            createSubjectIfNotExists(subjectRepository, "Architecture", "Technical Department");
            createSubjectIfNotExists(subjectRepository, "Electronics", "Technical Department");
            createSubjectIfNotExists(subjectRepository, "Mechanical Engineering", "Technical Department");
            createSubjectIfNotExists(subjectRepository, "Civil Engineering", "Technical Department");

            // Additional subjects
            createSubjectIfNotExists(subjectRepository, "Creative Writing", "English Department");
            createSubjectIfNotExists(subjectRepository, "Journalism", "English Department");
            createSubjectIfNotExists(subjectRepository, "Public Speaking", "English Department");
            createSubjectIfNotExists(subjectRepository, "World Cultures", "Social Studies Department");
            createSubjectIfNotExists(subjectRepository, "International Relations", "Social Studies Department");
            createSubjectIfNotExists(subjectRepository, "Law Studies", "Social Studies Department");

            System.out.println("Successfully initialized " + subjectRepository.count() + " subjects in the database.");

            // Note: Schedule creation should now happen when actual courses are created through the API
            // rather than being pre-populated with generic schedules
        };
    }

    private void createSubjectIfNotExists(SubjectRepository subjectRepository, String name, String department) {
        if (!subjectRepository.existsByName(name)) {
            Subject subject = Subject.builder()
                    .name(name)
                    .department(department)
                    .build();
            subjectRepository.save(subject);
            System.out.println("Created subject: " + name + " in " + department);
        }
    }
}