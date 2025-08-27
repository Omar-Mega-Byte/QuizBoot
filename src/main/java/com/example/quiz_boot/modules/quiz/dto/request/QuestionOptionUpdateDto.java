package com.example.quiz_boot.modules.quiz.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionOptionUpdateDto {

  @Size(min = 1, max = 200, message = "Option text must be between 1 and 200 characters")
  private String optionText;

  private Boolean isCorrect;

  private Integer optionOrder;

  @Size(max = 500, message = "Explanation cannot exceed 500 characters")
  private String explanation;
}
