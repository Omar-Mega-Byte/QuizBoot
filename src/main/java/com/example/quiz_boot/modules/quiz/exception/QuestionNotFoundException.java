package com.example.quiz_boot.modules.quiz.exception;

public class QuestionNotFoundException extends RuntimeException {
  public QuestionNotFoundException(String message) {
    super(message);
  }

  public QuestionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
