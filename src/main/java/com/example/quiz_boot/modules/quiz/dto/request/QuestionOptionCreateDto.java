package com.example.quiz_boot.modules.quiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionOptionCreateDto {

  @NotBlank(message = "Option text is required")
  @Size(min = 1, max = 200, message = "Option text must be between 1 and 200 characters")
  private String optionText;

  @NotNull(message = "isCorrect flag is required")
  private Boolean isCorrect;

  @NotNull(message = "Option order is required")
  private Integer optionOrder;

  @Size(max = 500, message = "Explanation cannot exceed 500 characters")
  private String explanation;
}
