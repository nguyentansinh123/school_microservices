package com.caffein.authservice.controller;


import com.caffein.authservice.model.User;
import com.caffein.authservice.request.roleRequest.UserRoleUpdateRequest;
import com.caffein.authservice.request.userRequest.ChangePasswordRequest;
import com.caffein.authservice.request.userRequest.ProfileUpdateRequest;
import com.caffein.authservice.request.userRequest.UserPermissionUpdateRequest;
import com.caffein.authservice.service.user.UserService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;

    @PatchMapping("/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateProfileInfo(@Valid final ProfileUpdateRequest request, final Authentication principal){
        this.userService.updateProfileInfo(request, getUserId(principal));

    }

    @PostMapping("/me/password")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid final ChangePasswordRequest request, final Authentication principal){
        this.userService.changePassword(request, getUserId(principal));

    }

    @PatchMapping("/deactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deactivateAccount(final Authentication principal){
        this.userService.deactivateAccount(getUserId(principal));
    }

    @PatchMapping("/reactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void reactivateAccount(final Authentication principal){
        this.userService.reactivateAccount(getUserId(principal));
    }

    @DeleteMapping("/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:delete')")
    public void deleteUser(final Authentication principal){
        this.userService.deleteAccount(getUserId(principal));
    }

    @PutMapping("/{userId}/roles")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('admin:write')")
    public void updateUserRoles(@PathVariable String userId, @RequestBody @Valid UserRoleUpdateRequest request) {
        userService.updateUserRoles(userId, request);
    }

    @PutMapping("/{userId}/permissions")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('admin:write')")
    public void updateUserPermissions(@PathVariable String userId, @RequestBody @Valid UserPermissionUpdateRequest request) {
        userService.updateUserPermissions(userId, request);
    }

    private String getUserId(final Authentication principal){
        return ((User) principal.getPrincipal()).getId();
    }

}
