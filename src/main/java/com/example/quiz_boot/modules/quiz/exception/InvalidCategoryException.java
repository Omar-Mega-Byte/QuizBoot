package com.example.quiz_boot.modules.quiz.exception;

public class InvalidCategoryException extends RuntimeException {
  public InvalidCategoryException(String message) {
    super(message);
  }

  public InvalidCategoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
