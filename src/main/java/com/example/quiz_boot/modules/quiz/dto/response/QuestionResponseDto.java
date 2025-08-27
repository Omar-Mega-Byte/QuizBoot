package com.example.quiz_boot.modules.quiz.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseDto {
  private Long id;
  private String questionText;
  private Long quizId;
  private List<QuestionOptionResponseDto> options;
  private String questionType;
  private int questionOrder;
  private int points;
  private String explanation;
  private boolean isRequired;
  private Instant createdAt;
  private Instant updatedAt;
}
