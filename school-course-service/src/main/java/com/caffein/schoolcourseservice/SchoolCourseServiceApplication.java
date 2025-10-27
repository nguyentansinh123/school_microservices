package com.caffein.schoolcourseservice;

import com.caffein.schoolcourseservice.model.DayOfWeek;
import com.caffein.schoolcourseservice.model.Schedule;
import com.caffein.schoolcourseservice.model.Subject;
import com.caffein.schoolcourseservice.repository.ScheduleRepository;
import com.caffein.schoolcourseservice.repository.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
            
            // Create schedules for subjects
            createSubjectSchedules(subjectRepository, scheduleRepository);
            
            System.out.println("Successfully initialized " + scheduleRepository.count() + " schedules in the database.");
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
    
    private void createSubjectSchedules(SubjectRepository subjectRepository, ScheduleRepository scheduleRepository) {
        // Create schedules for core academic subjects
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Mathematics", 
            List.of(
                new ScheduleInfo(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 30), "Room 101", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(9, 30), "Room 101", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(9, 30), "Room 101", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "English Literature",
            List.of(
                new ScheduleInfo(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 30), "Room 201", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(11, 30), "Room 201", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Physics",
            List.of(
                new ScheduleInfo(DayOfWeek.MONDAY, LocalTime.of(13, 0), LocalTime.of(14, 30), "Lab 301", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.WEDNESDAY, LocalTime.of(13, 0), LocalTime.of(14, 30), "Lab 301", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.FRIDAY, LocalTime.of(13, 0), LocalTime.of(15, 0), "Lab 301", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Chemistry",
            List.of(
                new ScheduleInfo(DayOfWeek.TUESDAY, LocalTime.of(13, 0), LocalTime.of(14, 30), "Lab 302", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.THURSDAY, LocalTime.of(13, 0), LocalTime.of(15, 0), "Lab 302", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Biology",
            List.of(
                new ScheduleInfo(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 30), "Lab 303", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(11, 30), "Lab 303", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Computer Science",
            List.of(
                new ScheduleInfo(DayOfWeek.MONDAY, LocalTime.of(15, 0), LocalTime.of(16, 30), "Computer Lab A", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.WEDNESDAY, LocalTime.of(15, 0), LocalTime.of(16, 30), "Computer Lab A", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.FRIDAY, LocalTime.of(15, 0), LocalTime.of(16, 30), "Computer Lab A", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "History",
            List.of(
                new ScheduleInfo(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(9, 30), "Room 401", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(9, 30), "Room 401", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "French",
            List.of(
                new ScheduleInfo(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 30), "Room 501", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0), LocalTime.of(12, 30), "Room 501", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Spanish",
            List.of(
                new ScheduleInfo(DayOfWeek.TUESDAY, LocalTime.of(11, 0), LocalTime.of(12, 30), "Room 502", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(12, 30), "Room 502", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Art and Design",
            List.of(
                new ScheduleInfo(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(12, 0), "Art Studio", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Music",
            List.of(
                new ScheduleInfo(DayOfWeek.TUESDAY, LocalTime.of(15, 0), LocalTime.of(16, 30), "Music Room", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.THURSDAY, LocalTime.of(15, 0), LocalTime.of(16, 30), "Music Room", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Physical Education",
            List.of(
                new ScheduleInfo(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(15, 30), "Gymnasium", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(15, 30), "Gymnasium", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(15, 30), "Gymnasium", "2024-2025", "Fall")
            ));
            
        createSchedulesForSubject(subjectRepository, scheduleRepository, "Business Studies",
            List.of(
                new ScheduleInfo(DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(15, 0), "Room 601", "2024-2025", "Fall"),
                new ScheduleInfo(DayOfWeek.THURSDAY, LocalTime.of(13, 30), LocalTime.of(15, 0), "Room 601", "2024-2025", "Fall")
            ));
    }
    
    private void createSchedulesForSubject(SubjectRepository subjectRepository, ScheduleRepository scheduleRepository, 
                                         String subjectName, List<ScheduleInfo> scheduleInfos) {
        Optional<Subject> subjectOpt = subjectRepository.findByName(subjectName);
        if (subjectOpt.isPresent()) {
            Subject subject = subjectOpt.get();
            
            for (ScheduleInfo info : scheduleInfos) {
                // Check if schedule already exists
                List<Schedule> existingSchedules = scheduleRepository.findBySubjectIdAndDayOfWeek(subject.getId(), info.dayOfWeek);
                boolean scheduleExists = existingSchedules.stream()
                    .anyMatch(s -> s.getStartTime().equals(info.startTime) && 
                                  s.getEndTime().equals(info.endTime) &&
                                  s.getAcademicYear().equals(info.academicYear) &&
                                  s.getSemester().equals(info.semester));
                
                if (!scheduleExists) {
                    Schedule schedule = Schedule.builder()
                        .subject(subject)
                        .dayOfWeek(info.dayOfWeek)
                        .startTime(info.startTime)
                        .endTime(info.endTime)
                        .roomNumber(info.roomNumber)
                        .academicYear(info.academicYear)
                        .semester(info.semester)
                        .build();
                    
                    scheduleRepository.save(schedule);
                    System.out.println("Created schedule for " + subjectName + " on " + info.dayOfWeek + 
                                     " from " + info.startTime + " to " + info.endTime + " in " + info.roomNumber);
                }
            }
        }
    }
    
    // Helper class for schedule information
    private static class ScheduleInfo {
        DayOfWeek dayOfWeek;
        LocalTime startTime;
        LocalTime endTime;
        String roomNumber;
        String academicYear;
        String semester;
        
        public ScheduleInfo(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, 
                          String roomNumber, String academicYear, String semester) {
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
            this.roomNumber = roomNumber;
            this.academicYear = academicYear;
            this.semester = semester;
        }
    }
}
