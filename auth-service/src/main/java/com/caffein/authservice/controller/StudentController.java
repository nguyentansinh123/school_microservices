package com.caffein.authservice.controller;

import com.caffein.authservice.kafka.producer.UserProducer;
import com.caffein.authservice.model.User;
import com.caffein.authservice.request.authRequest.RegistrationRequest;
import com.caffein.authservice.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@Tag(name = "student", description = "Create a student API")
public class StudentController {

    private final AuthenticationService service;
    private final UserProducer userProducer;

    @PostMapping("/admin/register-teacher")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> registerTeacher(@Valid @RequestBody final RegistrationRequest request) {
        User user = this.service.registerANewTeacher(request);
        userProducer.authToStudentTopic("pushUserFromAuthServiceToStudentServiceTopic", user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/admin/register-student")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> registerStudent(@Valid @RequestBody final RegistrationRequest request) {
        User user = this.service.registerANewStudent(request);
        userProducer.authToStudentTopic("pushUserFromAuthServiceToStudentServiceTopic", user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
