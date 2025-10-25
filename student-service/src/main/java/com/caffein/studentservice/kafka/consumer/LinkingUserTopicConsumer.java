package com.caffein.studentservice.kafka.consumer;

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
    }
}
