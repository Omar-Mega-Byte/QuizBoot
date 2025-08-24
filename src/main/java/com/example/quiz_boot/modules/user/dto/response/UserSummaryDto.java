package com.example.quiz_boot.modules.user.dto.response;

import lombok.Value;

/**
 * Immutable DTO for lightweight user information in lists or references
 * Contains minimal fields for performance and privacy
 */
@Value
public class UserSummaryDto {

    Long id;
    String username;
    String firstName;
    String lastName;
    boolean isActive;

}
