package com.example.quiz_boot.modules.quiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Quiz Exceptions
  @ExceptionHandler(InvalidQuizException.class)
  public ResponseEntity<String> handleInvalidQuizException(InvalidQuizException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(QuizNotFoundException.class)
  public ResponseEntity<String> handleQuizNotFoundException(QuizNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  // Category Exceptions
  @ExceptionHandler(InvalidCategoryException.class)
  public ResponseEntity<String> handleInvalidCategoryException(InvalidCategoryException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  // Question Exceptions
  @ExceptionHandler(InvalidQuestionException.class)
  public ResponseEntity<String> handleInvalidQuestionException(InvalidQuestionException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(QuestionNotFoundException.class)
  public ResponseEntity<String> handleQuestionNotFoundException(QuestionNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}
