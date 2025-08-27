package com.example.quiz_boot.modules.quiz.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOptionResponseDto {
  private Long id;
  private String optionText;
  private boolean isCorrect;
  private int optionOrder;
  private String explanation;
  private Instant createdAt;
  private Instant updatedAt;
}
