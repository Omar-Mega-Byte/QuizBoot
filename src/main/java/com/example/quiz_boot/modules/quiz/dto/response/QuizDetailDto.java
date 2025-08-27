package com.example.quiz_boot.modules.quiz.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDetailDto {
  private Long id;
  private String title;
  private String description;
  private CategoryResponseDto category;
  private List<QuestionResponseDto> questions;
  private int duration;
  private double passingScore;
  private int maxAttempts;
  private Instant createdAt;
  private Instant updatedAt;
}
