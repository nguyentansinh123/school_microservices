package com.caffein.authservice.service.user;

import com.caffein.authservice.exception.BusinessException;
import com.caffein.authservice.model.Permission;
import com.caffein.authservice.model.Role;
import com.caffein.authservice.model.User;
import com.caffein.authservice.repository.PermissionRepository;
import com.caffein.authservice.repository.RoleRepository;
import com.caffein.authservice.repository.UserRepository;
import com.caffein.authservice.request.roleRequest.UserRoleUpdateRequest;
import com.caffein.authservice.request.userRequest.ChangePasswordRequest;
import com.caffein.authservice.request.userRequest.ProfileUpdateRequest;
import com.caffein.authservice.request.userRequest.UserPermissionUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("Load User By Username Tests")
    class LoadUserByUsernameTests {

        @Test
        @DisplayName("Should load user successfully by email")
        void shouldLoadUserByEmailSuccessfully() {
            // Given
            String email = "user@mail.com";
            User user = User.builder()
                    .id("user-123")
                    .email(email)
                    .password("encodedPassword")
                    .build();

            when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));

            // When
            UserDetails result = userService.loadUserByUsername(email);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo(email);
            verify(userRepository).findByEmailIgnoreCase(email);
        }

        @Test
        @DisplayName("Should throw exception when user not found by email")
        void shouldThrowExceptionWhenUserNotFoundByEmail() {
            // Given
            String email = "nonexistent@mail.com";
            when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.loadUserByUsername(email))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessageContaining("User not found");

            verify(userRepository).findByEmailIgnoreCase(email);
        }

        @Test
        @DisplayName("Should handle case insensitive email lookup")
        void shouldHandleCaseInsensitiveEmail() {
            // Given
            String email = "USER@MAIL.COM";
            User user = User.builder()
                    .id("user-123")
                    .email("user@mail.com")
                    .build();

            when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));

            // When
            UserDetails result = userService.loadUserByUsername(email);

            // Then
            assertThat(result).isNotNull();
            verify(userRepository).findByEmailIgnoreCase(email);
        }
    }

    @Nested
    @DisplayName("Update Profile Info Tests")
    class UpdateProfileInfoTests {

        @Test
        @DisplayName("Should update profile info successfully")
        void shouldUpdateProfileInfoSuccessfully() {
            // Given
            String userId = "user-123";
            ProfileUpdateRequest request = new ProfileUpdateRequest();
            request.setFirstName("Updated");
            request.setLastName("Name");

            User user = User.builder()
                    .id(userId)
                    .firstName("Old")
                    .lastName("Name")
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            doNothing().when(userMapper).mergeUserInfo(user, request);
            when(userRepository.save(user)).thenReturn(user);

            // When
            userService.updateProfileInfo(request, userId);

            // Then
            verify(userRepository).findById(userId);
            verify(userMapper).mergeUserInfo(user, request);
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when user not found for profile update")
        void shouldThrowExceptionWhenUserNotFoundForProfileUpdate() {
            // Given
            String userId = "non-existent";
            ProfileUpdateRequest request = new ProfileUpdateRequest();

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.updateProfileInfo(request, userId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("User");

            verify(userRepository).findById(userId);
            verify(userMapper, never()).mergeUserInfo(any(), any());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Change Password Tests")
    class ChangePasswordTests {

        @Test
        @DisplayName("Should change password successfully with valid credentials")
        void shouldChangePasswordSuccessfully() {
            // Given
            String userId = "user-123";
            String currentPassword = "oldPassword123";
            String newPassword = "newPassword456";
            String encodedCurrentPassword = "encodedOldPassword";
            String encodedNewPassword = "encodedNewPassword";

            ChangePasswordRequest request = ChangePasswordRequest.builder()
                    .currentPassword(currentPassword)
                    .newPassword(newPassword)
                    .confirmNewPassword(newPassword)
                    .build();

            User user = User.builder()
                    .id(userId)
                    .email("user@mail.com")
                    .password(encodedCurrentPassword)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(currentPassword, encodedCurrentPassword)).thenReturn(true);
            when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
            when(userRepository.save(any(User.class))).thenReturn(user);

            // When
            userService.changePassword(request, userId);

            // Then
            verify(userRepository).findById(userId);
            verify(passwordEncoder).matches(currentPassword, encodedCurrentPassword);
            verify(passwordEncoder).encode(newPassword);
            verify(userRepository).save(user);
            assertThat(user.getPassword()).isEqualTo(encodedNewPassword);
        }

        @Test
        @DisplayName("Should throw exception when new password and confirmation do not match")
        void shouldThrowExceptionWhenPasswordsDoNotMatch() {
            // Given
            String userId = "user-123";
            ChangePasswordRequest request = ChangePasswordRequest.builder()
                    .currentPassword("oldPassword123")
                    .newPassword("newPassword456")
                    .confirmNewPassword("differentPassword")
                    .build();

            // When & Then
            assertThatThrownBy(() -> userService.changePassword(request, userId))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository, never()).findById(anyString());
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when current password is incorrect")
        void shouldThrowExceptionWhenCurrentPasswordIncorrect() {
            // Given
            String userId = "user-123";
            String currentPassword = "wrongPassword";
            String encodedPassword = "encodedCorrectPassword";

            ChangePasswordRequest request = ChangePasswordRequest.builder()
                    .currentPassword(currentPassword)
                    .newPassword("newPassword456")
                    .confirmNewPassword("newPassword456")
                    .build();

            User user = User.builder()
                    .id(userId)
                    .email("user@mail.com")
                    .password(encodedPassword)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(currentPassword, encodedPassword)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> userService.changePassword(request, userId))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).findById(userId);
            verify(passwordEncoder).matches(currentPassword, encodedPassword);
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Deactivate Account Tests")
    class DeactivateAccountTests {

        @Test
        @DisplayName("Should deactivate account successfully")
        void shouldDeactivateAccountSuccessfully() {
            // Given
            String userId = "user-123";
            User user = User.builder()
                    .id(userId)
                    .email("user@mail.com")
                    .enabled(true)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);

            // When
            userService.deactivateAccount(userId);

            // Then
            assertThat(user.isEnabled()).isFalse();
            verify(userRepository).findById(userId);
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when account already deactivated")
        void shouldThrowExceptionWhenAccountAlreadyDeactivated() {
            // Given
            String userId = "user-123";
            User user = User.builder()
                    .id(userId)
                    .enabled(false)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // When & Then
            assertThatThrownBy(() -> userService.deactivateAccount(userId))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user not found for deactivation")
        void shouldThrowExceptionWhenUserNotFoundForDeactivation() {
            // Given
            String userId = "non-existent";
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.deactivateAccount(userId))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Reactivate Account Tests")
    class ReactivateAccountTests {

        @Test
        @DisplayName("Should reactivate account successfully")
        void shouldReactivateAccountSuccessfully() {
            // Given
            String userId = "user-123";
            User user = User.builder()
                    .id(userId)
                    .email("user@mail.com")
                    .enabled(false)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);

            // When
            userService.reactivateAccount(userId);

            // Then
            assertThat(user.isEnabled()).isTrue();
            verify(userRepository).findById(userId);
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when account already active")
        void shouldThrowExceptionWhenAccountAlreadyActive() {
            // Given
            String userId = "user-123";
            User user = User.builder()
                    .id(userId)
                    .enabled(true)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // When & Then
            assertThatThrownBy(() -> userService.reactivateAccount(userId))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user not found for reactivation")
        void shouldThrowExceptionWhenUserNotFoundForReactivation() {
            // Given
            String userId = "non-existent";
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.reactivateAccount(userId))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Update User Roles Tests")
    class UpdateUserRolesTests {

        @Test
        @DisplayName("Should update user roles successfully")
        void shouldUpdateUserRolesSuccessfully() {
            // Given
            String userId = "user-123";
            List<String> roleNames = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
            UserRoleUpdateRequest request = new UserRoleUpdateRequest();
            request.setRoleNames(roleNames);

            User user = User.builder()
                    .id(userId)
                    .email("user@mail.com")
                    .build();

            Role roleUser = Role.builder().name("ROLE_USER").build();
            Role roleAdmin = Role.builder().name("ROLE_ADMIN").build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
            when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleAdmin));
            when(userRepository.save(user)).thenReturn(user);

            // When
            userService.updateUserRoles(userId, request);

            // Then
            assertThat(user.getRoles()).hasSize(2);
            assertThat(user.getRoles()).containsExactlyInAnyOrder(roleUser, roleAdmin);
            verify(userRepository).findById(userId);
            verify(roleRepository).findByName("ROLE_USER");
            verify(roleRepository).findByName("ROLE_ADMIN");
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when role not found")
        void shouldThrowExceptionWhenRoleNotFound() {
            // Given
            String userId = "user-123";
            List<String> roleNames = Arrays.asList("ROLE_INVALID");
            UserRoleUpdateRequest request = new UserRoleUpdateRequest();
            request.setRoleNames(roleNames);

            User user = User.builder().id(userId).build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(roleRepository.findByName("ROLE_INVALID")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.updateUserRoles(userId, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Role not found");

            verify(userRepository).findById(userId);
            verify(roleRepository).findByName("ROLE_INVALID");
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user not found for role update")
        void shouldThrowExceptionWhenUserNotFoundForRoleUpdate() {
            // Given
            String userId = "non-existent";
            UserRoleUpdateRequest request = new UserRoleUpdateRequest();
            request.setRoleNames(Arrays.asList("ROLE_USER"));

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.updateUserRoles(userId, request))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).findById(userId);
            verify(roleRepository, never()).findByName(anyString());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Update User Permissions Tests")
    class UpdateUserPermissionsTests {

        @Test
        @DisplayName("Should update user permissions successfully")
        void shouldUpdateUserPermissionsSuccessfully() {
            // Given
            String userId = "user-123";
            List<String> permissionNames = Arrays.asList("READ", "WRITE");
            UserPermissionUpdateRequest request = new UserPermissionUpdateRequest();
            request.setPermissionNames(permissionNames);

            User user = User.builder()
                    .id(userId)
                    .email("user@mail.com")
                    .build();

            Permission readPermission = Permission.builder().name("READ").build();
            Permission writePermission = Permission.builder().name("WRITE").build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(permissionRepository.findByName("READ")).thenReturn(Optional.of(readPermission));
            when(permissionRepository.findByName("WRITE")).thenReturn(Optional.of(writePermission));
            when(userRepository.save(user)).thenReturn(user);

            // When
            userService.updateUserPermissions(userId, request);

            // Then
            assertThat(user.getPermissions()).hasSize(2);
            assertThat(user.getPermissions()).containsExactlyInAnyOrder(readPermission, writePermission);
            verify(userRepository).findById(userId);
            verify(permissionRepository).findByName("READ");
            verify(permissionRepository).findByName("WRITE");
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw exception when permission not found")
        void shouldThrowExceptionWhenPermissionNotFound() {
            // Given
            String userId = "user-123";
            List<String> permissionNames = Arrays.asList("INVALID_PERMISSION");
            UserPermissionUpdateRequest request = new UserPermissionUpdateRequest();
            request.setPermissionNames(permissionNames);

            User user = User.builder().id(userId).build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(permissionRepository.findByName("INVALID_PERMISSION")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.updateUserPermissions(userId, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Permission not found");

            verify(userRepository).findById(userId);
            verify(permissionRepository).findByName("INVALID_PERMISSION");
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user not found for permission update")
        void shouldThrowExceptionWhenUserNotFoundForPermissionUpdate() {
            // Given
            String userId = "non-existent";
            UserPermissionUpdateRequest request = new UserPermissionUpdateRequest();
            request.setPermissionNames(Arrays.asList("READ"));

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.updateUserPermissions(userId, request))
                    .isInstanceOf(BusinessException.class);

            verify(userRepository).findById(userId);
            verify(permissionRepository, never()).findByName(anyString());
            verify(userRepository, never()).save(any());
        }
    }
}