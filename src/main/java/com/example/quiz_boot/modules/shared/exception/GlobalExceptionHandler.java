package com.example.quiz_boot.modules.shared.exception;

import com.example.quiz_boot.modules.quiz.exception.InvalidQuizException;
import com.example.quiz_boot.modules.quiz.exception.QuizNotFoundException;
import com.example.quiz_boot.modules.quiz.exception.CategoryNotFoundException;
import com.example.quiz_boot.modules.quiz.exception.QuestionNotFoundException;
import com.example.quiz_boot.modules.user.exception.InvalidUserException;
import com.example.quiz_boot.modules.user.exception.UserNotFoundException;
import com.example.quiz_boot.modules.user.exception.DatabaseOperationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Quiz Module Exceptions
  @ExceptionHandler(InvalidQuizException.class)
  public ResponseEntity<String> handleInvalidQuizException(InvalidQuizException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(QuizNotFoundException.class)
  public ResponseEntity<String> handleQuizNotFoundException(QuizNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(QuestionNotFoundException.class)
  public ResponseEntity<String> handleQuestionNotFoundException(QuestionNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  // User Module Exceptions
  @ExceptionHandler(InvalidUserException.class)
  public ResponseEntity<String> handleInvalidUserException(InvalidUserException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(DatabaseOperationException.class)
  public ResponseEntity<String> handleDatabaseOperationException(DatabaseOperationException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  // Validation Exceptions
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  // Generic Exception Handler
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred: " + ex.getMessage());
  }

  // IllegalArgumentException Handler
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
