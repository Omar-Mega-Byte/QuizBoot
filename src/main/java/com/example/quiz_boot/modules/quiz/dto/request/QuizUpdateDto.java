package com.example.quiz_boot.modules.quiz.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuizUpdateDto {

  @Size(min = 3, max = 200, message = "Quiz title must be between 3 and 200 characters")
  private String title;

  @Size(min = 10, max = 1000, message = "Quiz description must be between 10 and 1000 characters")
  private String description;

  private Long categoryId;

  @Valid
  private List<QuestionUpdateDto> questions;

  @Min(value = 0, message = "Duration must be at least 0")
  @Max(value = 3, message = "Duration cannot exceed 3 hours")
  private Integer duration;

  @Min(value = 0, message = "Passing score must be at least 0")
  @Max(value = 100, message = "Passing score cannot exceed 100")
  private Double passingScore;

  @Min(value = 1, message = "Max attempts must be at least 1")
  @Max(value = 10, message = "Max attempts cannot exceed 10")
  private Integer maxAttempts;
}
