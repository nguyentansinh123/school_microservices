package com.caffein.authservice.service.auth;


import com.caffein.authservice.model.User;
import com.caffein.authservice.request.authRequest.AuthenticationRequest;
import com.caffein.authservice.request.authRequest.RefreshRequest;
import com.caffein.authservice.request.authRequest.RegistrationRequest;
import com.caffein.authservice.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest request);

    User register(RegistrationRequest request);

    User registerANewStudent(RegistrationRequest request);

    User registerANewTeacher(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request);

}
