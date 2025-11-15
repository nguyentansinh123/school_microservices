package com.caffein.authservice.service.auth;

import com.caffein.authservice.config.jwtConfig.JwtService;
import com.caffein.authservice.exception.BusinessException;
import com.caffein.authservice.model.Role;
import com.caffein.authservice.model.User;
import com.caffein.authservice.repository.RoleRepository;
import com.caffein.authservice.repository.UserRepository;
import com.caffein.authservice.request.authRequest.AuthenticationRequest;
import com.caffein.authservice.request.authRequest.RefreshRequest;
import com.caffein.authservice.request.authRequest.RegistrationRequest;
import com.caffein.authservice.response.AuthenticationResponse;
import com.caffein.authservice.service.user.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationServiceImpl Unit Tests")
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void shouldLoginSuccessfully() {
            // Given
            String email = "ali@mail.com";
            String password = "pAssword1!_";
            String accessToken = "access.token.jwt";
            String refreshToken = "refresh.token.jwt";

            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail(email);
            request.setPassword(password);

            User user = User.builder()
                    .id("user-123")
                    .email(email)
                    .firstName("Ali")
                    .lastName("Bouali")
                    .password("encodedPassword")
                    .build();

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(user);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtService.generateAccessToken(email)).thenReturn(accessToken);
            when(jwtService.generateRefreshToken(email)).thenReturn(refreshToken);

            // When
            AuthenticationResponse response = authenticationService.login(request);

            // Then
            assertNotNull(response);
            assertEquals(accessToken, response.getAccessToken());
            assertEquals(refreshToken, response.getRefreshToken());
            assertEquals("Bearer", response.getTokenType());

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService).generateAccessToken(email);
            verify(jwtService).generateRefreshToken(email);
        }

        @Test
        @DisplayName("Should throw exception when credentials are invalid")
        void shouldThrowExceptionWhenCredentialsInvalid() {
            // Given
            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail("ali@mail.com");
            request.setPassword("wrongPassword");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            // When & Then
            assertThatThrownBy(() -> authenticationService.login(request))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessage("Invalid credentials");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, never()).generateAccessToken(anyString());
            verify(jwtService, never()).generateRefreshToken(anyString());
        }
    }

    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @Test
        @DisplayName("Should register a user successfully with valid request")
        void shouldRegisterUserSuccessfully() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setFirstName("Ali");
            request.setLastName("Bouali");
            request.setEmail("ali@mail.com");
            request.setPassword("pAssword1!_");
            request.setCofirmPassword("pAssword1!_");
            request.setPhoneNumber("+4912389765634");

            Role userRole = Role.builder()
                    .name("ROLE_USER")
                    .build();

            User mappedUser = User.builder()
                    .firstName("Ali")
                    .lastName("Bouali")
                    .email("ali@mail.com")
                    .password("encodedPassword")
                    .phoneNumber("+4912389765634")
                    .build();

            User savedUser = User.builder()
                    .id("user-123")
                    .firstName("Ali")
                    .lastName("Bouali")
                    .email("ali@mail.com")
                    .password("encodedPassword")
                    .phoneNumber("+4912389765634")
                    .roles(List.of(userRole))
                    .build();

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
            when(userMapper.toUser(request)).thenReturn(mappedUser);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(roleRepository.save(any(Role.class))).thenReturn(userRole);

            // When
            User result = authenticationService.register(request);

            // Then
            assertNotNull(result);
            assertEquals("Ali", result.getFirstName());
            assertEquals("Bouali", result.getLastName());
            assertEquals("ali@mail.com", result.getEmail());
            assertThat(result.getRoles()).hasSize(1);
            assertThat(result.getRoles().get(0).getName()).isEqualTo("ROLE_USER");

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            User capturedUser = userCaptor.getValue();
            assertThat(capturedUser.getRoles()).contains(userRole);

            verify(roleRepository).save(any(Role.class));
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("ali@mail.com");
            request.setPhoneNumber("+4912389765634");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> authenticationService.register(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Email");  

            verify(userRepository).existsByEmailIgnoreCase(request.getEmail());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when phone number already exists")
        void shouldThrowExceptionWhenPhoneNumberExists() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("ali@mail.com");
            request.setPhoneNumber("+4912389765634");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> authenticationService.register(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("phone number");  // Changed to match actual message

            verify(userRepository).existsByPhoneNumber(request.getPhoneNumber());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when passwords do not match")
        void shouldThrowExceptionWhenPasswordsDoNotMatch() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("ali@mail.com");
            request.setPhoneNumber("+4912389765634");
            request.setPassword("pAssword1!_");
            request.setCofirmPassword("differentPassword");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> authenticationService.register(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("password");  // Changed to lowercase

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when password is null")
        void shouldThrowExceptionWhenPasswordIsNull() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("ali@mail.com");
            request.setPhoneNumber("+4912389765634");
            request.setPassword(null);
            request.setCofirmPassword("pAssword1!_");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> authenticationService.register(request))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when ROLE_USER does not exist")
        void shouldThrowExceptionWhenRoleNotFound() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("ali@mail.com");
            request.setPhoneNumber("+4912389765634");
            request.setPassword("pAssword1!_");
            request.setCofirmPassword("pAssword1!_");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> authenticationService.register(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Role user does not exist");

            verify(roleRepository).findByName("ROLE_USER");
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Register Student Tests")
    class RegisterStudentTests {

        @Test
        @DisplayName("Should register a student successfully")
        void shouldRegisterStudentSuccessfully() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setFirstName("Student");
            request.setLastName("Test");
            request.setEmail("student@mail.com");
            request.setPassword("pAssword1!_");
            request.setCofirmPassword("pAssword1!_");
            request.setPhoneNumber("+4912389765635");

            Role studentRole = Role.builder()
                    .name("ROLE_STUDENT")
                    .build();

            User mappedUser = User.builder()
                    .firstName("Student")
                    .lastName("Test")
                    .email("student@mail.com")
                    .build();

            User savedUser = User.builder()
                    .id("student-123")
                    .firstName("Student")
                    .lastName("Test")
                    .email("student@mail.com")
                    .roles(List.of(studentRole))
                    .build();

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(roleRepository.findByName("ROLE_STUDENT")).thenReturn(Optional.of(studentRole));
            when(userMapper.toUser(request)).thenReturn(mappedUser);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(roleRepository.save(any(Role.class))).thenReturn(studentRole);

            // When
            User result = authenticationService.registerANewStudent(request);

            // Then
            assertNotNull(result);
            assertEquals("Student", result.getFirstName());
            assertEquals("student@mail.com", result.getEmail());
            assertThat(result.getRoles()).hasSize(1);
            assertThat(result.getRoles().get(0).getName()).isEqualTo("ROLE_STUDENT");

            verify(roleRepository).findByName("ROLE_STUDENT");
            verify(userRepository).save(any(User.class));
            verify(roleRepository).save(studentRole);
        }

        @Test
        @DisplayName("Should throw exception when ROLE_STUDENT does not exist")
        void shouldThrowExceptionWhenStudentRoleNotFound() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("student@mail.com");
            request.setPhoneNumber("+4912389765635");
            request.setPassword("pAssword1!_");
            request.setCofirmPassword("pAssword1!_");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(roleRepository.findByName("ROLE_STUDENT")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> authenticationService.registerANewStudent(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Role user does not exist");

            verify(roleRepository).findByName("ROLE_STUDENT");
        }

        @Test
        @DisplayName("Should throw exception when student email already exists")
        void shouldThrowExceptionWhenStudentEmailExists() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("student@mail.com");
            request.setPhoneNumber("+4912389765635");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> authenticationService.registerANewStudent(request))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).existsByEmailIgnoreCase(request.getEmail());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Register Teacher Tests")
    class RegisterTeacherTests {

        @Test
        @DisplayName("Should register a teacher successfully")
        void shouldRegisterTeacherSuccessfully() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setFirstName("Teacher");
            request.setLastName("Test");
            request.setEmail("teacher@mail.com");
            request.setPassword("pAssword1!_");
            request.setCofirmPassword("pAssword1!_");
            request.setPhoneNumber("+4912389765636");

            Role teacherRole = Role.builder()
                    .name("ROLE_TEACHER")
                    .build();

            User mappedUser = User.builder()
                    .firstName("Teacher")
                    .lastName("Test")
                    .email("teacher@mail.com")
                    .build();

            User savedUser = User.builder()
                    .id("teacher-123")
                    .firstName("Teacher")
                    .lastName("Test")
                    .email("teacher@mail.com")
                    .roles(List.of(teacherRole))
                    .build();

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(roleRepository.findByName("ROLE_TEACHER")).thenReturn(Optional.of(teacherRole));
            when(userMapper.toUser(request)).thenReturn(mappedUser);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(roleRepository.save(any(Role.class))).thenReturn(teacherRole);

            // When
            User result = authenticationService.registerANewTeacher(request);

            // Then
            assertNotNull(result);
            assertEquals("Teacher", result.getFirstName());
            assertEquals("teacher@mail.com", result.getEmail());
            assertThat(result.getRoles()).hasSize(1);
            assertThat(result.getRoles().get(0).getName()).isEqualTo("ROLE_TEACHER");

            verify(roleRepository).findByName("ROLE_TEACHER");
            verify(userRepository).save(any(User.class));
            verify(roleRepository).save(teacherRole);
        }

        @Test
        @DisplayName("Should throw exception when ROLE_TEACHER does not exist")
        void shouldThrowExceptionWhenTeacherRoleNotFound() {
            // Given
            RegistrationRequest request = new RegistrationRequest();
            request.setEmail("teacher@mail.com");
            request.setPhoneNumber("+4912389765636");
            request.setPassword("pAssword1!_");
            request.setCofirmPassword("pAssword1!_");

            when(userRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(false);
            when(userRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(roleRepository.findByName("ROLE_TEACHER")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> authenticationService.registerANewTeacher(request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Role user does not exist");

            verify(roleRepository).findByName("ROLE_TEACHER");
        }
    }

    @Nested
    @DisplayName("Refresh Token Tests")
    class RefreshTokenTests {

        @Test
        @DisplayName("Should refresh token successfully with valid refresh token")
        void shouldRefreshTokenSuccessfully() {
            // Given
            String refreshToken = "valid.refresh.token";
            String newAccessToken = "new.access.token";

            RefreshRequest request = new RefreshRequest();
            request.setRefreshToken(refreshToken);

            when(jwtService.refreshAccessToken(refreshToken)).thenReturn(newAccessToken);

            // When
            AuthenticationResponse response = authenticationService.refreshToken(request);

            // Then
            assertNotNull(response);
            assertEquals(newAccessToken, response.getAccessToken());
            assertEquals(refreshToken, response.getRefreshToken());
            assertEquals("Bearer", response.getTokenType());

            verify(jwtService).refreshAccessToken(refreshToken);
        }

        @Test
        @DisplayName("Should handle exception when refresh token is invalid")
        void shouldHandleExceptionWhenRefreshTokenInvalid() {
            // Given
            String refreshToken = "invalid.refresh.token";
            RefreshRequest request = new RefreshRequest();
            request.setRefreshToken(refreshToken);

            when(jwtService.refreshAccessToken(refreshToken))
                    .thenThrow(new RuntimeException("Invalid token"));

            // When & Then
            assertThatThrownBy(() -> authenticationService.refreshToken(request))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Invalid token");

            verify(jwtService).refreshAccessToken(refreshToken);
        }
    }
}
