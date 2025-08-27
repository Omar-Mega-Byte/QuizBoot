package com.example.quiz_boot.modules.quiz.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuizCreateDto {

  @NotBlank(message = "Quiz title is required")
  @Size(min = 3, max = 200, message = "Quiz title must be between 3 and 200 characters")
  private String title;

  @NotBlank(message = "Quiz description is required")
  @Size(min = 10, max = 1000, message = "Quiz description must be between 10 and 1000 characters")
  private String description;

  @NotNull(message = "Category ID is required")
  private Long categoryId;

  @NotNull(message = "Creator ID is required")
  private Long creatorId;

  @Valid
  private List<QuestionCreateDto> questions;

  @Min(value = 0, message = "Duration must be at least 0")
  @Max(value = 3, message = "Duration cannot exceed 3 hours")
  @NotNull(message = "Duration is required")
  private Integer duration;

  @Min(value = 0, message = "Passing score must be at least 0")
  @Max(value = 100, message = "Passing score cannot exceed 100")
  @NotNull(message = "Passing score is required")
  private Double passingScore;

  @Min(value = 1, message = "Max attempts must be at least 1")
  @Max(value = 10, message = "Max attempts cannot exceed 10")
  @NotNull(message = "Max attempts is required")
  private Integer maxAttempts;
}
