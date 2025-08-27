package com.example.quiz_boot.modules.quiz.dto.response;

import java.time.Instant;
import java.util.List;

import com.example.quiz_boot.modules.user.dto.response.UserSummaryDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDto {
  private Long id;
  private String title;
  private String description;
  private CategorySummaryDto category;
  private UserSummaryDto creator;
  private List<QuestionSummaryDto> questions;
  private int duration;
  private double passingScore;
  private int maxAttempts;
  private Instant createdAt;
  private Instant updatedAt;
}
