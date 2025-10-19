package com.caffein.authservice.request.authRequest;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RefreshRequest {
    private String refreshToken;
}
