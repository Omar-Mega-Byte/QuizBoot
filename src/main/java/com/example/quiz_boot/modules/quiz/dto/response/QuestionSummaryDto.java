package com.example.quiz_boot.modules.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSummaryDto {
  private Long id;
  private String questionText;
  private String questionType;
  private int questionOrder;
  private int points;
  private boolean isRequired;
}
