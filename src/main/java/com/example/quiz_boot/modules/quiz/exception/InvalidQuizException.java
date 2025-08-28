package com.example.quiz_boot.modules.quiz.exception;

public class InvalidQuizException extends RuntimeException {
  public InvalidQuizException(String message) {
    super(message);
  }

  public InvalidQuizException(String message, Throwable cause) {
    super(message, cause);
  }
}
