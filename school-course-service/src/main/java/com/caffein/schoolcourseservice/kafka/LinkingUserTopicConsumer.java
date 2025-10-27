package com.caffein.schoolcourseservice.kafka;

import com.caffein.schoolcourseservice.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LinkingUserTopicConsumer {

    @KafkaListener(topics = "pushUserFromAuthServiceToStudentServiceTopic", groupId = "mysuperGroup")
    public void consume(UserDTO message) {
        log.info("Received message from pushUserFromAuthServiceToStudentServiceTopic: {}", message);
    }
}
