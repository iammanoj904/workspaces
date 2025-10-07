package com.omni.main.ValidateUserNames.controller;

import com.omni.main.ValidateUserNames.dto.ApiResponse;
import com.omni.main.ValidateUserNames.dto.UserCreateRequest;
import com.omni.main.ValidateUserNames.dto.UsernameChangeRequest;
import com.omni.main.ValidateUserNames.service.UsernameService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dukan/username")
public class UsernameController {

    private final UsernameService usernameService;

    public UsernameController(UsernameService usernameService) {
        this.usernameService = usernameService;
    }

    @PostMapping("/create")
    public ApiResponse<Object> createUser(@Valid @RequestBody UserCreateRequest request) {
        return usernameService.createUser(request.getUserName(),request.getPhoneNumber(), request.getEmail());
    }

    @PutMapping("/change")
    public ApiResponse<Object> changeUsername(@Valid @RequestBody UsernameChangeRequest request) {
        return usernameService.changeUsername(request.getUserId(), request.getNewUsername());
    }
}