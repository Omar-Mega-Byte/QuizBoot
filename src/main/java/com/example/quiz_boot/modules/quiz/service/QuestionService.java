package com.example.quiz_boot.modules.quiz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.quiz_boot.modules.quiz.dto.request.QuestionCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.QuestionUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuestionResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuestionSummaryDto;
import com.example.quiz_boot.modules.quiz.exception.InvalidQuestionException;
import com.example.quiz_boot.modules.quiz.exception.QuestionNotFoundException;
import com.example.quiz_boot.modules.quiz.mapper.QuestionMapper;
import com.example.quiz_boot.modules.quiz.model.Question;
import com.example.quiz_boot.modules.quiz.repository.QuestionRepository;
import com.example.quiz_boot.modules.quiz.validation.QuestionValidation;

import jakarta.transaction.Transactional;

@Service
public class QuestionService {
  private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

  private final QuestionRepository questionRepository;
  private final QuestionValidation questionValidation;
  private final QuestionMapper questionMapper;

  public QuestionService(QuestionRepository questionRepository,
      QuestionValidation questionValidation,
      QuestionMapper questionMapper) {
    this.questionRepository = questionRepository;
    this.questionValidation = questionValidation;
    this.questionMapper = questionMapper;
  }

  @Transactional
  public QuestionResponseDto createQuestion(QuestionCreateDto questionCreateDto) {
    // Audit: Log question creation
    logger.info("AUDIT: Creating question for quiz ID: {}",
        questionCreateDto != null ? questionCreateDto.getQuizId() : "null");

    if (questionCreateDto == null) {
      logger.warn("AUDIT: Question creation failed: QuestionCreateDto is null");
      throw new InvalidQuestionException("Question creation data cannot be null");
    }

    if (!questionValidation.isValidForCreation(
        questionCreateDto.getQuestionText(),
        questionCreateDto.getQuizId(),
        questionCreateDto.getQuestionType(),
        questionCreateDto.getQuestionOrder(),
        questionCreateDto.getPoints(),
        questionCreateDto.getExplanation())) {
      logger.warn("AUDIT: Question creation failed: Invalid question data: {}", questionCreateDto);
      throw new InvalidQuestionException("Invalid question data provided");
    }

    // Validate options count for question type
    if (questionCreateDto.getOptions() != null &&
        !questionValidation.hasValidOptionsCount(
            questionCreateDto.getQuestionType(),
            questionCreateDto.getOptions().size())) {
      logger.warn("AUDIT: Question creation failed: Invalid options count for type: {}",
          questionCreateDto.getQuestionType());
      throw new InvalidQuestionException("Invalid number of options for question type");
    }

    try {
      // Create normalized question
      QuestionCreateDto normalizedQuestion = new QuestionCreateDto();
      normalizedQuestion.setQuestionText(questionCreateDto.getQuestionText().trim());
      normalizedQuestion.setQuizId(questionCreateDto.getQuizId());
      normalizedQuestion.setOptions(questionCreateDto.getOptions());
      normalizedQuestion.setQuestionType(questionCreateDto.getQuestionType().toUpperCase());
      normalizedQuestion.setQuestionOrder(questionCreateDto.getQuestionOrder());
      normalizedQuestion.setPoints(questionCreateDto.getPoints());
      normalizedQuestion.setExplanation(
          questionCreateDto.getExplanation() != null ? questionCreateDto.getExplanation().trim() : null);
      normalizedQuestion.setIsRequired(
          Boolean.TRUE.equals(questionCreateDto.getIsRequired()));

      // Convert to entity and save
      Question savedQuestion = questionRepository.save(questionMapper.toEntity(normalizedQuestion));

      // Audit: Log successful question creation
      logger.info("AUDIT: Question created successfully with ID: {}", savedQuestion.getId());

      return questionMapper.toResponseDto(savedQuestion);
    } catch (Exception e) {
      logger.error("AUDIT: Question creation failed due to database error: {}", e.getMessage());
      throw new InvalidQuestionException("Failed to create question due to database error", e);
    }
  }

  @Transactional
  public QuestionResponseDto updateQuestion(Long id, QuestionUpdateDto questionUpdateDto) {
    logger.info("AUDIT: Updating question with ID: {}", id);

    if (id == null || id <= 0) {
      throw new InvalidQuestionException("Question ID must be valid");
    }

    if (questionUpdateDto == null) {
      logger.warn("AUDIT: Question update failed: QuestionUpdateDto is null");
      throw new InvalidQuestionException("Question update data cannot be null");
    }

    if (!questionValidation.isValidForUpdate(
        questionUpdateDto.getQuestionText(),
        questionUpdateDto.getQuestionType(),
        questionUpdateDto.getQuestionOrder(),
        questionUpdateDto.getPoints(),
        questionUpdateDto.getExplanation())) {
      logger.warn("AUDIT: Question update failed: Invalid question data: {}", questionUpdateDto);
      throw new InvalidQuestionException("Invalid question data provided");
    }

    try {
      Question existingQuestion = questionRepository.findById(id)
          .orElseThrow(() -> new QuestionNotFoundException("Question not found with ID: " + id));

      // Create normalized update
      QuestionUpdateDto normalizedUpdate = new QuestionUpdateDto();
      normalizedUpdate.setQuestionText(questionUpdateDto.getQuestionText().trim());
      normalizedUpdate.setOptions(questionUpdateDto.getOptions());
      normalizedUpdate.setQuestionType(questionUpdateDto.getQuestionType().toUpperCase());
      normalizedUpdate.setQuestionOrder(questionUpdateDto.getQuestionOrder());
      normalizedUpdate.setPoints(questionUpdateDto.getPoints());
      normalizedUpdate.setExplanation(
          questionUpdateDto.getExplanation() != null ? questionUpdateDto.getExplanation().trim() : null);
      normalizedUpdate.setIsRequired(
          Boolean.TRUE.equals(questionUpdateDto.getIsRequired()));

      // Update entity
      questionMapper.updateEntity(existingQuestion, normalizedUpdate);
      Question updatedQuestion = questionRepository.save(existingQuestion);

      logger.info("AUDIT: Question updated successfully with ID: {}", updatedQuestion.getId());

      return questionMapper.toResponseDto(updatedQuestion);
    } catch (QuestionNotFoundException e) {
      logger.warn("AUDIT: Question update failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("AUDIT: Question update failed due to database error: {}", e.getMessage());
      throw new InvalidQuestionException("Failed to update question due to database error", e);
    }
  }

  public QuestionResponseDto getQuestionById(Long id) {
    logger.debug("Retrieving question with ID: {}", id);

    if (id == null || id <= 0) {
      throw new InvalidQuestionException("Question ID must be valid");
    }

    try {
      Question question = questionRepository.findById(id)
          .orElseThrow(() -> new QuestionNotFoundException("Question not found with ID: " + id));

      return questionMapper.toResponseDto(question);
    } catch (QuestionNotFoundException e) {
      logger.warn("Question retrieval failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("AUDIT: Question retrieval failed due to database error: {}", e.getMessage());
      throw new InvalidQuestionException("Failed to retrieve question due to database error", e);
    }
  }

  public List<QuestionSummaryDto> getQuestionsByQuizId(Long quizId) {
    logger.debug("Retrieving questions for quiz ID: {}", quizId);

    if (quizId == null || quizId <= 0) {
      throw new InvalidQuestionException("Quiz ID must be valid");
    }

    try {
      List<Question> questions = questionRepository.findByQuizId(quizId);
      return questionMapper.toSummaryDtoList(questions);
    } catch (Exception e) {
      logger.error("AUDIT: Questions retrieval failed due to database error: {}", e.getMessage());
      throw new InvalidQuestionException("Failed to retrieve questions due to database error", e);
    }
  }

  public Page<QuestionSummaryDto> getQuestionsByQuizId(Long quizId, Pageable pageable) {
    logger.debug("Retrieving questions for quiz ID: {} with pagination: {}", quizId, pageable);

    if (quizId == null || quizId <= 0) {
      throw new InvalidQuestionException("Quiz ID must be valid");
    }

    try {
      Page<Question> questions = questionRepository.findAll(pageable);
      return questions.map(questionMapper::toSummaryDto);
    } catch (Exception e) {
      logger.error("AUDIT: Questions retrieval failed due to database error: {}", e.getMessage());
      throw new InvalidQuestionException("Failed to retrieve questions due to database error", e);
    }
  }

  @Transactional
  public void deleteQuestion(Long id) {
    logger.info("AUDIT: Deleting question with ID: {}", id);

    if (id == null || id <= 0) {
      throw new InvalidQuestionException("Question ID must be valid");
    }

    try {
      if (!questionRepository.existsById(id)) {
        throw new QuestionNotFoundException("Question not found with ID: " + id);
      }

      questionRepository.deleteById(id);
      logger.info("AUDIT: Question deleted successfully with ID: {}", id);
    } catch (QuestionNotFoundException e) {
      logger.warn("AUDIT: Question deletion failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("AUDIT: Question deletion failed due to database error: {}", e.getMessage());
      throw new InvalidQuestionException("Failed to delete question due to database error", e);
    }
  }

  public boolean existsById(Long id) {
    if (id == null || id <= 0) {
      return false;
    }

    try {
      return questionRepository.existsById(id);
    } catch (Exception e) {
      logger.error("Error checking question existence: {}", e.getMessage());
      return false;
    }
  }

  public long countByQuizId(Long quizId) {
    if (quizId == null || quizId <= 0) {
      return 0;
    }

    try {
      return questionRepository.findByQuizId(quizId).size();
    } catch (Exception e) {
      logger.error("Error counting questions for quiz: {}", e.getMessage());
      return 0;
    }
  }
}
