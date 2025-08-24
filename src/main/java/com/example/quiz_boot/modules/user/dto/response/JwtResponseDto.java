package com.example.quiz_boot.modules.user.dto.response;

import lombok.Value;

/**
 * Immutable DTO for JWT authentication responses
 */
@Value
public class JwtResponseDto {
    String token;
    String type;
    UserResponseDto user;

    public static JwtResponseDto of(String token, UserResponseDto user) {
        return new JwtResponseDto(token, "Bearer", user);
    }
}
