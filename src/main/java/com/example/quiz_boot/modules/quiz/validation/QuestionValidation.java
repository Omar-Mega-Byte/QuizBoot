package com.example.quiz_boot.modules.quiz.validation;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class QuestionValidation {

  /**
   * Valid question types
   */
  private static final List<String> VALID_QUESTION_TYPES = List.of("MULTIPLE_CHOICE", "TRUE_FALSE", "FILL_IN_BLANK");

  /**
   * Validates question text
   */
  public boolean isValidQuestionText(String questionText) {
    return questionText != null &&
        !questionText.trim().isEmpty() &&
        questionText.length() >= 10 &&
        questionText.length() <= 500;
  }

  /**
   * Validates quiz ID
   */
  public boolean isValidQuizId(Long quizId) {
    return quizId != null && quizId > 0;
  }

  /**
   * Validates question type
   */
  public boolean isValidQuestionType(String questionType) {
    return questionType != null && VALID_QUESTION_TYPES.contains(questionType.toUpperCase());
  }

  /**
   * Validates question order
   */
  public boolean isValidQuestionOrder(Integer order) {
    return order != null && order >= 1 && order <= 100;
  }

  /**
   * Validates question points
   */
  public boolean isValidPoints(Integer points) {
    return points != null && points >= 1 && points <= 10;
  }

  /**
   * Validates explanation
   */
  public boolean isValidExplanation(String explanation) {
    return explanation == null || explanation.length() <= 1000;
  }

  /**
   * Validates minimum options for multiple choice questions
   */
  public boolean hasValidOptionsCount(String questionType, int optionsCount) {
    if ("MULTIPLE_CHOICE".equals(questionType)) {
      return optionsCount >= 2 && optionsCount <= 6;
    }
    if ("TRUE_FALSE".equals(questionType)) {
      return optionsCount == 2;
    }
    if ("FILL_IN_BLANK".equals(questionType)) {
      return optionsCount == 0; // No options needed
    }
    return false;
  }

  /**
   * Validates that at least one option is correct for multiple choice
   */
  public boolean hasCorrectAnswer(List<Boolean> correctAnswers) {
    return correctAnswers != null &&
        !correctAnswers.isEmpty() &&
        correctAnswers.stream().anyMatch(Boolean::booleanValue);
  }

  /**
   * Complete validation for question creation
   */
  public boolean isValidForCreation(String questionText, Long quizId, String questionType,
      Integer order, Integer points, String explanation) {
    return isValidQuestionText(questionText) &&
        isValidQuizId(quizId) &&
        isValidQuestionType(questionType) &&
        isValidQuestionOrder(order) &&
        isValidPoints(points) &&
        isValidExplanation(explanation);
  }

  /**
   * Complete validation for question update
   */
  public boolean isValidForUpdate(String questionText, String questionType,
      Integer order, Integer points, String explanation) {
    return isValidQuestionText(questionText) &&
        isValidQuestionType(questionType) &&
        isValidQuestionOrder(order) &&
        isValidPoints(points) &&
        isValidExplanation(explanation);
  }
}
