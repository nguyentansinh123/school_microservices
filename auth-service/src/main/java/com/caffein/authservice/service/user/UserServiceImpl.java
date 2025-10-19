package com.caffein.authservice.service.user;

import com.caffein.authservice.exception.BusinessException;
import com.caffein.authservice.exception.ErrorCode;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.caffein.authservice.exception.ErrorCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + userEmail));
    }

    @Override
    public void updateProfileInfo(ProfileUpdateRequest request, String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND, userId));

        this.userMapper.mergeUserInfo(savedUser, request);
        this.userRepository.save(savedUser);

    }

    @Override
    public void changePassword(ChangePasswordRequest request, String userId) {

        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new BusinessException(ErrorCode.CHANGE_PASSWORD_MISMATCH);
        }

        final User savedUser = this.userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        if (!this.passwordEncoder.matches(request.getCurrentPassword(), savedUser.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }
        final String encoded = this.passwordEncoder.encode(request.getNewPassword());
        savedUser.setPassword(encoded);
        this.userRepository.save(savedUser);

    }

    @Override
    public void deactivateAccount(String userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);
        }
        user.setEnabled(false);
        this.userRepository.save(user);

    }

    @Override
    public void reactivateAccount(String userId) {
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        if (user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);
        }

        user.setEnabled(true);
        this.userRepository.save(user);
    }

    @Override
    public void deleteAccount(String userId) {

    }

    @Override
    public void updateUserRoles(String userId, UserRoleUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND, userId));

        List<Role> roles = request.getRoleNames().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName)))
                .collect(Collectors.toList());

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public void updateUserPermissions(String userId, UserPermissionUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND, userId));

        List<Permission> permissions = request.getPermissionNames().stream()
                .map(permissionName -> permissionRepository.findByName(permissionName)
                        .orElseThrow(() -> new EntityNotFoundException("Permission not found: " + permissionName)))
                .collect(Collectors.toList());

        user.setPermissions(permissions);
        userRepository.save(user);
    }
}
