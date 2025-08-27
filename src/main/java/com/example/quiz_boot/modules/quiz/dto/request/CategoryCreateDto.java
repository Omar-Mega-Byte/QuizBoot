package com.example.quiz_boot.modules.quiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateDto {

  @NotBlank(message = "Category name is required")
  @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
  private String name;

  @Size(max = 1000, message = "Description cannot exceed 1000 characters")
  private String description;
}
