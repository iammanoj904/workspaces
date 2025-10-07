package com.omni.main.ValidateUserNames.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserCreateRequest {

    @NotBlank(message = "Name must not be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Name must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one digit")
    private String userName;

    @NotBlank(message = "Phone number must not be null")
    @Pattern(
            regexp = "^(?:\\+971|971)(50|52|54|55|56|58)\\d{7}$",
            message = "Phone number must be a valid UAE mobile number (e.g., +971501234567)")
    private String phoneNumber;

    @NotBlank(message = "Email must not be null")
    @Email(message = "Email must be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$",
            message = "Email must end with .com"
    )
    private String email;

}