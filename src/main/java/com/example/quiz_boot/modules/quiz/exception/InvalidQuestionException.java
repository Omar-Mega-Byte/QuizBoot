package com.example.quiz_boot.modules.quiz.exception;

public class InvalidQuestionException extends RuntimeException {
  public InvalidQuestionException(String message) {
    super(message);
  }

  public InvalidQuestionException(String message, Throwable cause) {
    super(message, cause);
  }
}
