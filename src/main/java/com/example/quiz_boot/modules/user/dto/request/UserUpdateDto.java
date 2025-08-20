package com.example.quiz_boot.modules.user.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for updating existing user information
 * Contains only fields that users should be allowed to modify
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String email;

    private String firstName;

    private String lastName;

    // Note: Username is excluded to prevent confusion and maintain consistency
    // Note: Password updates should use a separate ChangePasswordDto for security
    // Note: isActive should only be modified by admins through separate endpoints
    // Note: System fields (id, timestamps) are never user-modifiable
}
