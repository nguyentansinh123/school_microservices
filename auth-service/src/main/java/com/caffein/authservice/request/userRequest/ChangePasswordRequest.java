package com.caffein.authservice.request.userRequest;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
