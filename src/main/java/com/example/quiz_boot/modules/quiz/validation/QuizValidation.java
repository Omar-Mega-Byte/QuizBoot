package com.example.quiz_boot.modules.quiz.validation;

import org.springframework.stereotype.Component;

@Component
public class QuizValidation {

  /**
   * Validates quiz title
   */
  public boolean isValidTitle(String title) {
    return title != null &&
        !title.trim().isEmpty() &&
        title.length() >= 3 &&
        title.length() <= 200;
  }

  /**
   * Validates quiz description
   */
  public boolean isValidDescription(String description) {
    return description != null &&
        !description.trim().isEmpty() &&
        description.length() >= 10 &&
        description.length() <= 1000;
  }

  /**
   * Validates category ID
   */
  public boolean isValidCategoryId(Long categoryId) {
    return categoryId != null && categoryId > 0;
  }

  /**
   * Validates creator ID
   */
  public boolean isValidCreatorId(Long creatorId) {
    return creatorId != null && creatorId > 0;
  }

  /**
   * Validates time limit (in minutes)
   */
  public boolean isValidTimeLimit(Integer timeLimit) {
    return timeLimit == null || (timeLimit > 0 && timeLimit <= 480); // Max 8 hours
  }

  /**
   * Validates if quiz has minimum required questions
   */
  public boolean hasMinimumQuestions(int questionCount) {
    return questionCount >= 1;
  }

  /**
   * Complete validation for quiz creation
   */
  public boolean isValidForCreation(String title, String description, Long categoryId, Long creatorId) {
    return isValidTitle(title) &&
        isValidDescription(description) &&
        isValidCategoryId(categoryId) &&
        isValidCreatorId(creatorId);
  }

  /**
   * Complete validation for quiz update
   */
  public boolean isValidForUpdate(String title, String description, Long categoryId) {
    return isValidTitle(title) &&
        isValidDescription(description) &&
        isValidCategoryId(categoryId);
  }
}
