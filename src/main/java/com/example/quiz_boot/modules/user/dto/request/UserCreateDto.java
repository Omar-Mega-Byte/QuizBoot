package com.example.quiz_boot.modules.user.dto.request;

import lombok.Value;

/**
 * Immutable DTO for creating a new user
 * Contains only the fields that should be provided by the client when creating
 * a user
 */
@Value
public class UserCreateDto {

    String username;
    String email;
    String password;
    String firstName;
    String lastName;
    String role; // Role name: STUDENT, TEACHER, ADMIN

}
