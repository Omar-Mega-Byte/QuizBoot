package com.example.quiz_boot.modules.quiz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.quiz_boot.modules.quiz.dto.request.QuizCreateDto;
import com.example.quiz_boot.modules.quiz.exception.InvalidQuizException;
import com.example.quiz_boot.modules.quiz.mapper.QuizMapper;
import com.example.quiz_boot.modules.quiz.model.Quiz;
import com.example.quiz_boot.modules.quiz.repository.QuizRepository;
import com.example.quiz_boot.modules.quiz.validation.QuizValidation;
import com.example.quiz_boot.modules.user.exception.DatabaseOperationException;

import jakarta.transaction.Transactional;

@Service
public class QuizService {
  private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

  private final QuizRepository quizRepository;
  private final QuizValidation quizValidation;
  private final QuizMapper quizMapper;

  public QuizService(QuizRepository quizRepository, QuizValidation quizValidation, QuizMapper quizMapper) {
    this.quizRepository = quizRepository;
    this.quizValidation = quizValidation;
    this.quizMapper = quizMapper;
  }

  @Transactional
  public Long createQuiz(QuizCreateDto quizCreateDto) {
    if (quizCreateDto == null) {
      logger.warn("AUDIT: Quiz creation failed: QuizCreateDto is null");
      throw new InvalidQuizException("Quiz creation data cannot be null");
    }

    sanitizeQuizData(quizCreateDto);

    if (!quizValidation.isValidForCreation(
        quizCreateDto.getTitle(),
        quizCreateDto.getDescription(),
        quizCreateDto.getCategoryId(),
        quizCreateDto.getCreatorId())) {
      logger.warn("AUDIT: Quiz creation failed: Invalid quiz data for quiz: {}",
          quizCreateDto.getTitle());
      throw new InvalidQuizException("Invalid quiz data provided");
    }

    try {
      Quiz saved = quizRepository.save(quizMapper.toEntity(quizCreateDto));
      logger.info("AUDIT: Quiz created successfully with ID: {}", saved.getId());
      return saved.getId();
    } catch (DataAccessException e) {
      logger.error("AUDIT: Quiz creation failed due to DB error", e);
      // I know I shouldn't use Classes from User module :)
      throw new DatabaseOperationException("Failed to create quiz", e);
    } catch (Exception e) {
      logger.error("AUDIT: Quiz creation failed due to unexpected error", e);
      throw new InvalidQuizException("Failed to create quiz", e);
    }
  }

  private void sanitizeQuizData(QuizCreateDto dto) {
    dto.setTitle(dto.getTitle() != null ? dto.getTitle().trim() : null);
    dto.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
  }

}
