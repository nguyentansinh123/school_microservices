package com.caffein.authservice.kafka.producer.kafkaDTO;

import com.caffein.authservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserKafkaDTOMapper {

    public UserKafkaDTO toKafkaDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserKafkaDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .profilePictureUrl(user.getProfilePictureUrl())
                .roles(user.getRoles())
                .permissions(user.getPermissions())
                .build();
    }
}