package com.caffein.authservice.kafka.producer;

import com.caffein.authservice.model.User;
import com.caffein.authservice.request.authRequest.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProducer {

    private final KafkaTemplate<String, RegistrationRequest> kafkaTemplate;

    public void authToStudentTopic(String topic, RegistrationRequest request) {
        Message<RegistrationRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        kafkaTemplate.send(message);
    }

}
