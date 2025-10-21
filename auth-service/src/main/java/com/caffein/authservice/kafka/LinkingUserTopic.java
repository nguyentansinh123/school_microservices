package com.caffein.authservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class LinkingUserTopic {

    @Bean
    public NewTopic pushingUserTopic() {
        return TopicBuilder
                .name("pushUserFromAuthServiceToStudentServiceTopic")
                .build();
    }
}
