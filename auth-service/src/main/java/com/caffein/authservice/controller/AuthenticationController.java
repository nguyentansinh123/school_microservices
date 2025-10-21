package com.caffein.authservice.controller;

import com.caffein.authservice.kafka.producer.UserProducer;
import com.caffein.authservice.request.authRequest.AuthenticationRequest;
import com.caffein.authservice.request.authRequest.RefreshRequest;
import com.caffein.authservice.request.authRequest.RegistrationRequest;
import com.caffein.authservice.response.AuthenticationResponse;
import com.caffein.authservice.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserProducer userProducer;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid
            @RequestBody
            final AuthenticationRequest request) {
        return ResponseEntity.ok(this.service.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody final RegistrationRequest request) {
        this.service.register(request);
        userProducer.authToStudentTopic("pushUserFromAuthServiceToStudentServiceTopic", request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @RequestBody
            final RefreshRequest req) {
        return ResponseEntity.ok(this.service.refreshToken(req));
    }
}
