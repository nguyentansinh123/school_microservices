package com.caffein.authservice.kafka.producer;

import com.caffein.authservice.kafka.producer.kafkaDTO.UserKafkaDTO;
import com.caffein.authservice.kafka.producer.kafkaDTO.UserKafkaDTOMapper;
import com.caffein.authservice.model.User;
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

    private final KafkaTemplate<String, UserKafkaDTO> kafkaTemplate;
    private final UserKafkaDTOMapper userKafkaDTOMapper;

    public void authToStudentTopic(String topic, User request) {
        UserKafkaDTO userKafkaDTO = userKafkaDTOMapper.toKafkaDTO(request);
        
        Message<UserKafkaDTO> message = MessageBuilder
                .withPayload(userKafkaDTO)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        kafkaTemplate.send(message);
        log.info("User data sent to Kafka topic: {} with id: {}", topic, userKafkaDTO.getId());
    }
}
