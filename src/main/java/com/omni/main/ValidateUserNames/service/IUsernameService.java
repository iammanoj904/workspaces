package com.omni.main.ValidateUserNames.service;

import com.omni.main.ValidateUserNames.dto.ApiResponse;

public interface IUsernameService {

    ApiResponse<Object> createUser(String username, String phoneNumber, String email);
    ApiResponse<Object> changeUsername(Long userId, String newUsername);
}