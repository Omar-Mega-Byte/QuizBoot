package com.example.quiz_boot.modules.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for lightweight user information in lists or references
 * Contains minimal fields for performance and privacy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {

    private Long id;

    private String username;

    private String firstName;
    private String lastName;

    private boolean isActive;

}
