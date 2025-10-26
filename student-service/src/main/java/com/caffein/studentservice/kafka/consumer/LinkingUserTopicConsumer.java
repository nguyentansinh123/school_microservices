package com.caffein.studentservice.kafka.consumer;

import com.caffein.studentservice.dto.RoleDTO;
import com.caffein.studentservice.dto.UserDTO;
import com.caffein.studentservice.service.studentService.IStudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkingUserTopicConsumer {
    private final ObjectMapper objectMapper;
    private final IStudentService studentService;

    public LinkingUserTopicConsumer(ObjectMapper objectMapper, IStudentService studentService) {
        this.objectMapper = objectMapper;
        this.studentService = studentService;
    }

    @KafkaListener(topics = "pushUserFromAuthServiceToStudentServiceTopic", groupId = "mysuperGroup")
    public void consume(UserDTO message) {
        log.info("Received message from pushUserFromAuthServiceToStudentServiceTopic: {}", message);

        try {
            // Check if user has ROLE_STUDENT
            if (message.getRoles() != null && message.getRoles().stream()
                    .anyMatch(role -> "ROLE_STUDENT".equals(role.getName()))) {

                log.info("Processing student user: {} {} with email: {}",
                        message.getFirstName(), message.getLastName(), message.getEmail());

                // Save student to database
                studentService.createStudentFromUser(message);

            } else {
                log.debug("Ignoring non-student user with roles: {}",
                        message.getRoles() != null ?
                                message.getRoles().stream().map(RoleDTO::getName).toList() :
                                "No roles");
            }
        } catch (Exception e) {
            log.error("Error processing user message: {}", message, e);
        }
    }
}