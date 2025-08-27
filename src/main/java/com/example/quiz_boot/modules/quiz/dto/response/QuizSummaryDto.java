package com.example.quiz_boot.modules.quiz.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
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
}
