package com.caffein.authservice.request.roleRequest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRoleUpdateRequest {
    @NotEmpty(message = "Role names cannot be empty")
    private List<String> roleNames;
}