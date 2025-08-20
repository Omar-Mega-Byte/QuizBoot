package com.example.quiz_boot.modules.user.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning user information to clients
 * Excludes sensitive information like passwords
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;

    private String username;
    private String email;

    private String firstName;
    private String lastName;

    private boolean isActive;

    private Instant createdAt;
    private Instant updatedAt;
}
