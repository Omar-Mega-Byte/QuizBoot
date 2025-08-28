package com.example.quiz_boot.modules.quiz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.quiz_boot.modules.quiz.dto.request.QuizCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.QuizUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizDetailDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizSummaryDto;
import com.example.quiz_boot.modules.quiz.exception.InvalidQuizException;
import com.example.quiz_boot.modules.quiz.exception.QuizNotFoundException;
import com.example.quiz_boot.modules.quiz.mapper.QuizMapper;
import com.example.quiz_boot.modules.quiz.model.Quiz;
import com.example.quiz_boot.modules.quiz.repository.QuizRepository;
import com.example.quiz_boot.modules.quiz.validation.QuizValidation;

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
    public QuizResponseDto createQuiz(QuizCreateDto quizCreateDto) {
        // Audit: Log quiz creation
        logger.info("AUDIT: Creating quiz: {}",
                quizCreateDto != null ? quizCreateDto.getTitle() : "null");

        if (quizCreateDto == null) {
            logger.warn("AUDIT: Quiz creation failed: QuizCreateDto is null");
            throw new InvalidQuizException("Quiz creation data cannot be null");
        }

        if (!quizValidation.isValidForCreation(
                quizCreateDto.getTitle(),
                quizCreateDto.getDescription(),
                quizCreateDto.getCategoryId(),
                quizCreateDto.getCreatorId())) {
            logger.warn("AUDIT: Quiz creation failed: Invalid quiz data: {}", quizCreateDto);
            throw new InvalidQuizException("Invalid quiz data provided");
        }

        try {
            // Create normalized quiz
            QuizCreateDto normalizedQuiz = new QuizCreateDto();
            normalizedQuiz.setTitle(quizCreateDto.getTitle().trim());
            normalizedQuiz.setDescription(
                    quizCreateDto.getDescription() != null ? quizCreateDto.getDescription().trim() : null);
            normalizedQuiz.setCategoryId(quizCreateDto.getCategoryId());
            normalizedQuiz.setCreatorId(quizCreateDto.getCreatorId());
            normalizedQuiz.setQuestions(quizCreateDto.getQuestions());
            normalizedQuiz.setDuration(quizCreateDto.getDuration());
            normalizedQuiz.setPassingScore(quizCreateDto.getPassingScore());
            normalizedQuiz.setMaxAttempts(quizCreateDto.getMaxAttempts());

            // Convert to entity and save
            Quiz savedQuiz = quizRepository.save(quizMapper.toEntity(normalizedQuiz));

            // Audit: Log successful quiz creation
            logger.info("AUDIT: Quiz created successfully with ID: {}", savedQuiz.getId());

            return quizMapper.toResponseDto(savedQuiz);
        } catch (Exception e) {
            logger.error("AUDIT: Quiz creation failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to create quiz due to database error", e);
        }
    }

    @Transactional
    public QuizResponseDto updateQuiz(Long id, QuizUpdateDto quizUpdateDto) {
        logger.info("AUDIT: Updating quiz with ID: {}", id);

        if (id == null || id <= 0) {
            throw new InvalidQuizException("Quiz ID must be valid");
        }

        if (quizUpdateDto == null) {
            logger.warn("AUDIT: Quiz update failed: QuizUpdateDto is null");
            throw new InvalidQuizException("Quiz update data cannot be null");
        }

        if (!quizValidation.isValidForUpdate(
                quizUpdateDto.getTitle(),
                quizUpdateDto.getDescription(),
                quizUpdateDto.getCategoryId())) {
            logger.warn("AUDIT: Quiz update failed: Invalid quiz data: {}", quizUpdateDto);
            throw new InvalidQuizException("Invalid quiz data provided");
        }

        try {
            Quiz existingQuiz = quizRepository.findById(id)
                    .orElseThrow(() -> new QuizNotFoundException("Quiz not found with ID: " + id));

            // Create normalized update
            QuizUpdateDto normalizedUpdate = new QuizUpdateDto();
            normalizedUpdate.setTitle(quizUpdateDto.getTitle().trim());
            normalizedUpdate.setDescription(
                    quizUpdateDto.getDescription() != null ? quizUpdateDto.getDescription().trim() : null);
            normalizedUpdate.setCategoryId(quizUpdateDto.getCategoryId());
            normalizedUpdate.setQuestions(quizUpdateDto.getQuestions());
            normalizedUpdate.setDuration(quizUpdateDto.getDuration());
            normalizedUpdate.setPassingScore(quizUpdateDto.getPassingScore());
            normalizedUpdate.setMaxAttempts(quizUpdateDto.getMaxAttempts());

            // Update entity
            quizMapper.updateEntity(existingQuiz, normalizedUpdate);
            Quiz updatedQuiz = quizRepository.save(existingQuiz);

            logger.info("AUDIT: Quiz updated successfully with ID: {}", updatedQuiz.getId());

            return quizMapper.toResponseDto(updatedQuiz);
        } catch (QuizNotFoundException e) {
            logger.warn("AUDIT: Quiz update failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("AUDIT: Quiz update failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to update quiz due to database error", e);
        }
    }

    public QuizDetailDto getQuizById(Long id) {
        logger.debug("Retrieving quiz with ID: {}", id);

        if (id == null || id <= 0) {
            throw new InvalidQuizException("Quiz ID must be valid");
        }

        try {
            Quiz quiz = quizRepository.findById(id)
                    .orElseThrow(() -> new QuizNotFoundException("Quiz not found with ID: " + id));

            return quizMapper.toDetailDto(quiz);
        } catch (QuizNotFoundException e) {
            logger.warn("Quiz retrieval failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("AUDIT: Quiz retrieval failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to retrieve quiz due to database error", e);
        }
    }

    public List<QuizSummaryDto> getAllQuizzes() {
        logger.debug("Retrieving all quizzes");

        try {
            List<Quiz> quizzes = quizRepository.findAll();
            return quizMapper.toSummaryDtoList(quizzes);
        } catch (Exception e) {
            logger.error("AUDIT: Quizzes retrieval failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to retrieve quizzes due to database error", e);
        }
    }

    public Page<QuizSummaryDto> getAllQuizzes(Pageable pageable) {
        logger.debug("Retrieving quizzes with pagination: {}", pageable);

        try {
            Page<Quiz> quizzes = quizRepository.findAll(pageable);
            return quizzes.map(quizMapper::toSummaryDto);
        } catch (Exception e) {
            logger.error("AUDIT: Quizzes retrieval failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to retrieve quizzes due to database error", e);
        }
    }

    public List<QuizSummaryDto> getQuizzesByCategory(Long categoryId) {
        logger.debug("Retrieving quizzes for category ID: {}", categoryId);

        if (categoryId == null || categoryId <= 0) {
            throw new InvalidQuizException("Category ID must be valid");
        }

        try {
            List<Quiz> quizzes = quizRepository.findByCategoryId(categoryId);
            return quizMapper.toSummaryDtoList(quizzes);
        } catch (Exception e) {
            logger.error("AUDIT: Quizzes retrieval by category failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to retrieve quizzes by category due to database error", e);
        }
    }

    public List<QuizSummaryDto> getQuizzesByCreator(Long creatorId) {
        logger.debug("Retrieving quizzes for creator ID: {}", creatorId);

        if (creatorId == null || creatorId <= 0) {
            throw new InvalidQuizException("Creator ID must be valid");
        }

        try {
            List<Quiz> quizzes = quizRepository.findByIsCreatorId(creatorId);
            return quizMapper.toSummaryDtoList(quizzes);
        } catch (Exception e) {
            logger.error("AUDIT: Quizzes retrieval by creator failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to retrieve quizzes by creator due to database error", e);
        }
    }

    public Page<QuizSummaryDto> getQuizzesByCategory(Long categoryId, Pageable pageable) {
        logger.debug("Retrieving paginated quizzes for category ID: {} with pagination: {}", categoryId, pageable);

        if (categoryId == null || categoryId <= 0) {
            throw new InvalidQuizException("Category ID must be valid");
        }

        try {
            // Since repository doesn't have paginated method, we'll use a workaround
            // First get all quizzes by category, then create a Page manually
            List<Quiz> allQuizzes = quizRepository.findByCategoryId(categoryId);

            // Calculate pagination manually
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allQuizzes.size());

            if (start > allQuizzes.size()) {
                // Return empty page if start is beyond the list
                return Page.empty(pageable);
            }

            List<Quiz> pageContent = allQuizzes.subList(start, end);
            List<QuizSummaryDto> dtoContent = quizMapper.toSummaryDtoList(pageContent);

            return new PageImpl<>(dtoContent, pageable, allQuizzes.size());
        } catch (Exception e) {
            logger.error("AUDIT: Paginated quizzes retrieval by category failed due to database error: {}",
                    e.getMessage());
            throw new InvalidQuizException("Failed to retrieve paginated quizzes by category due to database error", e);
        }
    }

    public Page<QuizSummaryDto> getQuizzesByCreator(Long creatorId, Pageable pageable) {
        logger.debug("Retrieving paginated quizzes for creator ID: {} with pagination: {}", creatorId, pageable);

        if (creatorId == null || creatorId <= 0) {
            throw new InvalidQuizException("Creator ID must be valid");
        }

        try {
            // Since repository doesn't have paginated method, we'll use a workaround
            // First get all quizzes by creator, then create a Page manually
            List<Quiz> allQuizzes = quizRepository.findByIsCreatorId(creatorId);

            // Calculate pagination manually
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allQuizzes.size());

            if (start > allQuizzes.size()) {
                // Return empty page if start is beyond the list
                return Page.empty(pageable);
            }

            List<Quiz> pageContent = allQuizzes.subList(start, end);
            List<QuizSummaryDto> dtoContent = quizMapper.toSummaryDtoList(pageContent);

            return new PageImpl<>(dtoContent, pageable, allQuizzes.size());
        } catch (Exception e) {
            logger.error("AUDIT: Paginated quizzes retrieval by creator failed due to database error: {}",
                    e.getMessage());
            throw new InvalidQuizException("Failed to retrieve paginated quizzes by creator due to database error", e);
        }
    }

    @Transactional
    public void deleteQuiz(Long id) {
        logger.info("AUDIT: Deleting quiz with ID: {}", id);

        if (id == null || id <= 0) {
            throw new InvalidQuizException("Quiz ID must be valid");
        }

        try {
            if (!quizRepository.existsById(id)) {
                throw new QuizNotFoundException("Quiz not found with ID: " + id);
            }

            quizRepository.deleteById(id);
            logger.info("AUDIT: Quiz deleted successfully with ID: {}", id);
        } catch (QuizNotFoundException e) {
            logger.warn("AUDIT: Quiz deletion failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("AUDIT: Quiz deletion failed due to database error: {}", e.getMessage());
            throw new InvalidQuizException("Failed to delete quiz due to database error", e);
        }
    }

    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        try {
            return quizRepository.existsById(id);
        } catch (Exception e) {
            logger.error("Error checking quiz existence: {}", e.getMessage());
            return false;
        }
    }

    public long countQuizzesByCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return 0;
        }

        try {
            return quizRepository.findByCategoryId(categoryId).size();
        } catch (Exception e) {
            logger.error("Error counting quizzes for category: {}", e.getMessage());
            return 0;
        }
    }

    public long countQuizzesByCreator(Long creatorId) {
        if (creatorId == null || creatorId <= 0) {
            return 0;
        }

        try {
            return quizRepository.findByIsCreatorId(creatorId).size();
        } catch (Exception e) {
            logger.error("Error counting quizzes for creator: {}", e.getMessage());
            return 0;
        }
    }
}
