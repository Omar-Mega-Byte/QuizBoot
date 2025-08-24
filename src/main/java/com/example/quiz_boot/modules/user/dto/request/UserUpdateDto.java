package com.example.quiz_boot.modules.user.dto.request;

import lombok.Value;

/**
 * Immutable DTO for updating existing user information
 * Contains only fields that users should be allowed to modify
 */
@Value
public class UserUpdateDto {

    String email;
    String firstName;
    String lastName;

    // Note: Username is excluded to prevent confusion and maintain consistency
    // Note: Password updates should use a separate ChangePasswordDto for security
    // Note: isActive should only be modified by admins through separate endpoints
    // Note: System fields (id, timestamps) are never user-modifiable
}
