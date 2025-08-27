package com.example.quiz_boot.modules.quiz.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.quiz_boot.modules.quiz.dto.request.QuestionCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.QuestionUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuestionResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuestionSummaryDto;
import com.example.quiz_boot.modules.quiz.model.Question;

/**
 * Simple mapper for Question entity and DTOs
 * Provides conversion methods between Question entity and various DTOs
 */
@Component
public class QuestionMapper {

  @Autowired
  private QuestionOptionMapper questionOptionMapper;

  /**
   * Convert QuestionCreateDto to Question entity
   */
  public Question toEntity(QuestionCreateDto dto) {
    Question question = new Question();
    question.setQuestionText(dto.getQuestionText());
    question.setQuestionType(dto.getQuestionType());
    question.setQuestionOrder(dto.getQuestionOrder());
    question.setPoints(dto.getPoints());
    question.setExplanation(dto.getExplanation());
    question.setRequired(Boolean.TRUE.equals(dto.getIsRequired()));
    return question;
  }

  /**
   * Convert Question entity to QuestionResponseDto
   */
  public QuestionResponseDto toResponseDto(Question question) {
    return new QuestionResponseDto(
        question.getId(),
        question.getQuestionText(),
        question.getQuiz() != null ? question.getQuiz().getId() : null,
        question.getOptions() != null ? questionOptionMapper.toResponseDtoList(question.getOptions()) : null,
        question.getQuestionType(),
        question.getQuestionOrder(),
        question.getPoints(),
        question.getExplanation(),
        question.isRequired(),
        question.getCreatedAt(),
        question.getUpdatedAt());
  }

  /**
   * Convert Question entity to QuestionSummaryDto
   */
  public QuestionSummaryDto toSummaryDto(Question question) {
    return new QuestionSummaryDto(
        question.getId(),
        question.getQuestionText(),
        question.getQuestionType(),
        question.getQuestionOrder(),
        question.getPoints(),
        question.isRequired());
  }

  /**
   * Convert list of Question entities to list of QuestionResponseDto
   */
  public List<QuestionResponseDto> toResponseDtoList(List<Question> questions) {
    return questions.stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
  }

  /**
   * Convert list of Question entities to list of QuestionSummaryDto
   */
  public List<QuestionSummaryDto> toSummaryDtoList(List<Question> questions) {
    return questions.stream()
        .map(this::toSummaryDto)
        .collect(Collectors.toList());
  }

  /**
   * Update existing Question entity with QuestionUpdateDto
   */
  public void updateEntity(Question question, QuestionUpdateDto dto) {
    if (dto.getQuestionText() != null) {
      question.setQuestionText(dto.getQuestionText());
    }
    if (dto.getQuestionType() != null) {
      question.setQuestionType(dto.getQuestionType());
    }
    if (dto.getQuestionOrder() != null) {
      question.setQuestionOrder(dto.getQuestionOrder());
    }
    if (dto.getPoints() != null) {
      question.setPoints(dto.getPoints());
    }
    if (dto.getExplanation() != null) {
      question.setExplanation(dto.getExplanation());
    }
    if (dto.getIsRequired() != null) {
      question.setRequired(dto.getIsRequired());
    }
  }
}
