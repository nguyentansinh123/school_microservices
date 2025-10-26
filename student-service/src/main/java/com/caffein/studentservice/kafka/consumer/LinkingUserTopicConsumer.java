package com.caffein.studentservice.kafka.consumer;

import com.caffein.studentservice.dto.RoleDTO;
import com.caffein.studentservice.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkingUserTopicConsumer {
    private final ObjectMapper objectMapper;

    public LinkingUserTopicConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "pushUserFromAuthServiceToStudentServiceTopic", groupId = "mysuperGroup")
    public void consume(UserDTO message) {
        log.info("consume message from pushUserFromAuthServiceToStudentServiceTopic:{}", message);

        if (message.getRoles() != null && message.getRoles().stream()
                .anyMatch(role -> "ROLE_STUDENT".equals(role.getName()))) {
            log.info("Processing student user: {} with email: {}",
                    message.getFirstName() + " " + message.getLastName(),
                    message.getEmail());
        } else {
            log.debug("Ignoring non-student user with roles: {}",
                    message.getRoles() != null ?
                            message.getRoles().stream().map(RoleDTO::getName).toList() :
                            "No roles");
        }
    }
}
