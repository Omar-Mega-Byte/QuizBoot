package com.example.quiz_boot.modules.quiz.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionUpdateDto {

  @Size(min = 10, max = 500, message = "Question text must be between 10 and 500 characters")
  private String questionText;

  @Valid
  private List<QuestionOptionUpdateDto> options;

  private String questionType; // MULTIPLE_CHOICE, TRUE_FALSE, FILL_IN_BLANK

  @Min(value = 1, message = "Question order must be at least 1")
  @Max(value = 100, message = "Question order cannot exceed 100")
  private Integer questionOrder;

  @Min(value = 1, message = "Points must be at least 1")
  @Max(value = 10, message = "Points cannot exceed 10")
  private Integer points;

  @Size(max = 1000, message = "Explanation cannot exceed 1000 characters")
  private String explanation;

  private Boolean isRequired;
}
