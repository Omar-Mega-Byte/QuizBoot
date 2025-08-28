package com.example.quiz_boot.modules.quiz.validation;

import org.springframework.stereotype.Component;

@Component
public class CategoryValidation {

  /**
   * Validates category name
   */
  public boolean isValidName(String name) {
    return name != null &&
        !name.trim().isEmpty() &&
        name.length() >= 2 &&
        name.length() <= 100;
  }

  /**
   * Validates category description
   */
  public boolean isValidDescription(String description) {
    return description == null || description.length() <= 1000;
  }

  /**
   * Validates if category name is unique format (basic check)
   */
  public boolean hasValidFormat(String name) {
    if (name == null)
      return false;
    // Only letters, numbers, spaces, and basic punctuation
    return name.matches("^[a-zA-Z0-9\\s\\-_.,()]+$");
  }

  /**
   * Complete validation for category creation
   */
  public boolean isValidForCreation(String name, String description) {
    return isValidName(name) &&
        isValidDescription(description) &&
        hasValidFormat(name);
  }

  /**
   * Complete validation for category update
   */
  public boolean isValidForUpdate(String name, String description) {
    return isValidForCreation(name, description);
  }
}
