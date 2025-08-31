package com.example.quiz_boot.modules.quiz.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.quiz_boot.modules.quiz.dto.request.QuizCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.QuizUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizDetailDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizSummaryDto;
import com.example.quiz_boot.modules.quiz.model.Category;
import com.example.quiz_boot.modules.quiz.model.Quiz;
import com.example.quiz_boot.modules.quiz.repository.CategoryRepository;
import com.example.quiz_boot.modules.user.mapper.UserMapper;
import com.example.quiz_boot.modules.user.model.User;
import com.example.quiz_boot.modules.user.repository.UserRepository;

/**
 * Simple mapper for Quiz entity and DTOs
 * Provides conversion methods between Quiz entity and various DTOs
 */
@Component
public class QuizMapper {

  @Autowired
  private CategoryMapper categoryMapper;

  @Autowired
  private QuestionMapper questionMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * Convert QuizCreateDto to Quiz entity
   */
  public Quiz toEntity(QuizCreateDto dto) {
    Quiz quiz = new Quiz();
    quiz.setTitle(dto.getTitle());
    quiz.setDescription(dto.getDescription());

    // Set category entity
    if (dto.getCategoryId() != null) {
      Category category = categoryRepository.findById(dto.getCategoryId())
          .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
      quiz.setCategory(category);
    }

    // Set creator entity
    if (dto.getCreatorId() != null) {
      User creator = userRepository.findById(dto.getCreatorId())
          .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getCreatorId()));
      quiz.setCreator(creator);
    }

    quiz.setDuration(dto.getDuration());
    quiz.setPassingScore(dto.getPassingScore());
    quiz.setMaxAttempts(dto.getMaxAttempts());
    return quiz;
  }

  /**
   * Convert Quiz entity to QuizResponseDto
   */
  public QuizResponseDto toResponseDto(Quiz quiz) {
    return new QuizResponseDto(
        quiz.getId(),
        quiz.getTitle(),
        quiz.getDescription(),
        quiz.getCategory() != null ? categoryMapper.toSummaryDto(quiz.getCategory()) : null,
        quiz.getCreator() != null ? userMapper.toSummaryDto(quiz.getCreator()) : null,
        quiz.getQuestions() != null ? questionMapper.toSummaryDtoList(quiz.getQuestions()) : null,
        quiz.getDuration(),
        quiz.getPassingScore(),
        quiz.getMaxAttempts(),
        quiz.getCreatedAt(),
        quiz.getUpdatedAt());
  }

  /**
   * Convert Quiz entity to QuizSummaryDto
   */
  public QuizSummaryDto toSummaryDto(Quiz quiz) {
    return new QuizSummaryDto(
        quiz.getId(),
        quiz.getTitle(),
        quiz.getDescription(),
        quiz.getCategory() != null ? categoryMapper.toSummaryDto(quiz.getCategory()) : null,
        quiz.getDuration(),
        quiz.getPassingScore(),
        quiz.getQuestions() != null ? quiz.getQuestions().size() : 0,
        quiz.getCreatedAt());
  }

  /**
   * Convert Quiz entity to QuizDetailDto
   */
  public QuizDetailDto toDetailDto(Quiz quiz) {
    return new QuizDetailDto(
        quiz.getId(),
        quiz.getTitle(),
        quiz.getDescription(),
        quiz.getCategory() != null ? categoryMapper.toResponseDto(quiz.getCategory()) : null,
        quiz.getQuestions() != null ? questionMapper.toResponseDtoList(quiz.getQuestions()) : null,
        quiz.getDuration(),
        quiz.getPassingScore(),
        quiz.getMaxAttempts(),
        quiz.getCreatedAt(),
        quiz.getUpdatedAt());
  }

  /**
   * Convert list of Quiz entities to list of QuizResponseDto
   */
  public List<QuizResponseDto> toResponseDtoList(List<Quiz> quizzes) {
    return quizzes.stream()
        .map(this::toResponseDto)
        .collect(Collectors.toList());
  }

  /**
   * Convert list of Quiz entities to list of QuizSummaryDto
   */
  public List<QuizSummaryDto> toSummaryDtoList(List<Quiz> quizzes) {
    return quizzes.stream()
        .map(this::toSummaryDto)
        .collect(Collectors.toList());
  }

  /**
   * Update existing Quiz entity with QuizUpdateDto
   */
  public void updateEntity(Quiz quiz, QuizUpdateDto dto) {
    if (dto.getTitle() != null) {
      quiz.setTitle(dto.getTitle());
    }
    if (dto.getDescription() != null) {
      quiz.setDescription(dto.getDescription());
    }
    if (dto.getDuration() != null) {
      quiz.setDuration(dto.getDuration());
    }
    if (dto.getPassingScore() != null) {
      quiz.setPassingScore(dto.getPassingScore());
    }
    if (dto.getMaxAttempts() != null) {
      quiz.setMaxAttempts(dto.getMaxAttempts());
    }
  }

  /**
   * Convert QuizResponseDto to QuizSummaryDto
   * Used when we need to return a summary view of a detailed quiz response
   */
  public QuizSummaryDto toSummaryDto(QuizResponseDto responseDto) {
    return new QuizSummaryDto(
        responseDto.getId(),
        responseDto.getTitle(),
        responseDto.getDescription(),
        responseDto.getCategory(),
        responseDto.getDuration(),
        responseDto.getPassingScore(),
        responseDto.getQuestions() != null ? responseDto.getQuestions().size() : 0,
        responseDto.getCreatedAt());
  }
}
