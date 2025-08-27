package com.example.quiz_boot.modules.quiz.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.quiz_boot.modules.quiz.dto.request.CategoryCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.CategoryUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.CategoryResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.CategorySummaryDto;
import com.example.quiz_boot.modules.quiz.model.Category;

/**
 * Simple mapper for Category entity and DTOs
 * Provides conversion methods between Category entity and various DTOs
 */
@Component
public class CategoryMapper {

  /**
   * Convert CategoryCreateDto to Category entity
   */
  public Category toEntity(CategoryCreateDto dto) {
    Category category = new Category();
    category.setName(dto.getName());
    category.setDescription(dto.getDescription());
    return category;
  }

  /**
   * Convert Category entity to CategoryResponseDto
   */
  public CategoryResponseDto toResponseDto(Category category) {
    return new CategoryResponseDto(
        category.getId(),
        category.getName(),
        category.getDescription(),
        category.getCreatedAt(),
        category.getUpdatedAt());
  }

  /**
   * Convert Category entity to CategorySummaryDto
   */
  public CategorySummaryDto toSummaryDto(Category category) {
    return new CategorySummaryDto(
        category.getId(),
        category.getName());
  }

  /**
   * Convert list of Category entities to list of CategoryResponseDto
   */
  public List<CategoryResponseDto> toResponseDtoList(List<Category> categories) {
    return categories.stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
  }

  /**
   * Convert list of Category entities to list of CategorySummaryDto
   */
  public List<CategorySummaryDto> toSummaryDtoList(List<Category> categories) {
    return categories.stream()
        .map(this::toSummaryDto)
        .collect(Collectors.toList());
  }

  /**
   * Update existing Category entity with CategoryUpdateDto
   */
  public void updateEntity(Category category, CategoryUpdateDto dto) {
    if (dto.getName() != null) {
      category.setName(dto.getName());
    }
    if (dto.getDescription() != null) {
      category.setDescription(dto.getDescription());
    }
  }
}
