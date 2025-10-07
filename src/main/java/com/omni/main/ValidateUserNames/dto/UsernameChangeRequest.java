package com.omni.main.ValidateUserNames.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UsernameChangeRequest {

    @NotNull(message = "User ID must not be null")
    @Min(value = 1, message = "User ID must be a positive number")
    private Long userId;

    @NotBlank(message = "Name must not be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Name must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit"
    )
    private String newUsername;

}