package com.example.quiz_boot.modules.quiz.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
  private Long id;
  private String name;
  private String description;
  private Instant createdAt;
  private Instant updatedAt;
}
