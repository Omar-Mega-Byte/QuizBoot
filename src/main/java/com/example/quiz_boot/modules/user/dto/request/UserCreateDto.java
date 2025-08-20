package com.example.quiz_boot.modules.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new user
 * Contains only the fields that should be provided by the client when creating
 * a user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

}
