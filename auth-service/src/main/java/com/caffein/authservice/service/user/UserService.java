package com.caffein.authservice.service.user;

import com.caffein.authservice.request.userRequest.ChangePasswordRequest;
import com.caffein.authservice.request.userRequest.ProfileUpdateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService{

    void updateProfileInfo(ProfileUpdateRequest request, String userId);

    void changePassword(ChangePasswordRequest request, String userId);

    void deactivateAccount(String userId);

    void reactivateAccount(String userId);

    void deleteAccount(String userId);
}
