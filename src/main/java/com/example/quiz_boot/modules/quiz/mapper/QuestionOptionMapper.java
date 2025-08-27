package com.example.quiz_boot.modules.quiz.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.quiz_boot.modules.quiz.dto.request.QuestionOptionCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.QuestionOptionUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuestionOptionResponseDto;
import com.example.quiz_boot.modules.quiz.model.QuestionOption;

/**
 * Simple mapper for QuestionOption entity and DTOs
 * Provides conversion methods between QuestionOption entity and various DTOs
 */
@Component
public class QuestionOptionMapper {

  /**
   * Convert QuestionOptionCreateDto to QuestionOption entity
   */
  public QuestionOption toEntity(QuestionOptionCreateDto dto) {
    QuestionOption option = new QuestionOption();
    option.setOptionText(dto.getOptionText());
    option.setCorrect(dto.getIsCorrect());
    option.setOptionOrder(dto.getOptionOrder());
    option.setExplanation(dto.getExplanation());
    return option;
  }

  /**
   * Convert QuestionOption entity to QuestionOptionResponseDto
   */
  public QuestionOptionResponseDto toResponseDto(QuestionOption option) {
    return new QuestionOptionResponseDto(
        option.getId(),
        option.getOptionText(),
        option.isCorrect(),
        option.getOptionOrder(),
        option.getExplanation(),
        option.getCreatedAt(),
        option.getUpdatedAt());
  }

  /**
   * Convert list of QuestionOption entities to list of QuestionOptionResponseDto
   */
  public List<QuestionOptionResponseDto> toResponseDtoList(List<QuestionOption> options) {
    return options.stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
  }

  /**
   * Update existing QuestionOption entity with QuestionOptionUpdateDto
   */
  public void updateEntity(QuestionOption option, QuestionOptionUpdateDto dto) {
    if (dto.getOptionText() != null) {
      option.setOptionText(dto.getOptionText());
    }
    if (dto.getIsCorrect() != null) {
      option.setCorrect(dto.getIsCorrect());
    }
    if (dto.getOptionOrder() != null) {
      option.setOptionOrder(dto.getOptionOrder());
    }
    if (dto.getExplanation() != null) {
      option.setExplanation(dto.getExplanation());
    }
  }
}
