package com.example.quiz_boot.modules.user.dto.request;

import lombok.Value;

/**
 * Immutable DTO for user login requests
 */
@Value
public class LoginRequestDto {
    String username;
    String password;
}
