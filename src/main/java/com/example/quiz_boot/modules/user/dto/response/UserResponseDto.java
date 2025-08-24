package com.example.quiz_boot.modules.user.dto.response;

import java.time.Instant;
import java.util.Set;

import lombok.Value;

/**
 * Immutable DTO for returning user information to clients
 * Excludes sensitive information like passwords
 */
@Value
public class UserResponseDto {

    Long id;
    String username;
    String email;
    String firstName;
    String lastName;
    boolean isActive;
    Set<String> roles;
    Instant createdAt;
    Instant updatedAt;
}
