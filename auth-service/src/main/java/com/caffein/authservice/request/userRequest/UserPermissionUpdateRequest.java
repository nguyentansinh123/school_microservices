package com.caffein.authservice.request.userRequest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserPermissionUpdateRequest {
    @NotEmpty(message = "Permission names cannot be empty")
    private List<String> permissionNames;
}
