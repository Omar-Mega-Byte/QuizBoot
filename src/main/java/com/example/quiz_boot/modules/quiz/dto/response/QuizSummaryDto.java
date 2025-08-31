package com.example.quiz_boot.modules.quiz.dto.response;

import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// @AllArgsConstructor
@NoArgsConstructor
public class QuizSummaryDto {
  private Long id;
  private String title;
  private String description;
  private CategorySummaryDto category;
  private int duration;
  private double passingScore;
  private int questionCount;
  private Instant createdAt;

  // Constructor matching the arguments in QuizMapper
  public QuizSummaryDto(Long id, String title, String description, CategorySummaryDto category, int duration,
      double passingScore, int questionCount, Instant createdAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.category = category;
    this.duration = duration;
    this.passingScore = passingScore;
    this.questionCount = questionCount;
    this.createdAt = createdAt;
  }
}
