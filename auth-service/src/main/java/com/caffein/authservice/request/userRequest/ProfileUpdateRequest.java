package com.caffein.authservice.request.userRequest;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
