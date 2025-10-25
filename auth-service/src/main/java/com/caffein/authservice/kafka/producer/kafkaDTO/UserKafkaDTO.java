package com.caffein.authservice.kafka.producer.kafkaDTO;

import com.caffein.authservice.model.Permission;
import com.caffein.authservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserKafkaDTO {
    
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String profilePictureUrl;
    private List<Role> roles;
    private List<Permission> permissions;
}
