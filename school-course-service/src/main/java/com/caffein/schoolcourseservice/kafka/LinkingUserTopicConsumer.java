package com.caffein.schoolcourseservice.kafka;

import com.caffein.schoolcourseservice.dto.RoleDTO;
import com.caffein.schoolcourseservice.dto.UserDTO;
import com.caffein.schoolcourseservice.service.teacherService.ITeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LinkingUserTopicConsumer {

    private final ITeacherService teacherService;

    @KafkaListener(topics = "pushUserFromAuthServiceToStudentServiceTopic", groupId = "mysuperGroupnumbertwo")
    public void consume(UserDTO message) {
        log.info("Received message from pushUserFromAuthServiceToStudentServiceTopic: {}", message);

        try {
            // Check if user has ROLE_TEACHER
            if (message.getRoles() != null && message.getRoles().stream()
                    .anyMatch(role -> "ROLE_TEACHER".equals(role.getName()))) {

                log.info("Processing teacher: {} {} with email: {}",
                        message.getFirstName(), message.getLastName(), message.getEmail());

                // Save teacher to database
                teacherService.createTeacherFromUser(message);

                log.info("Successfully processed teacher user: {}", message.getEmail());
            } else {
                log.debug("Ignoring non-teacher user with roles: {}",
                        message.getRoles() != null ?
                                message.getRoles().stream().map(RoleDTO::getName).toList() :
                                "No roles");
            }
        } catch (Exception e) {
            log.error("Error processing user message: {}", message, e);
            // retry logic
        }
    }
}
