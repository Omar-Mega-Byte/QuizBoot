package com.example.quiz_boot.modules.quiz.controller;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.quiz_boot.modules.quiz.dto.request.QuizCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.QuizUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizDetailDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuizSummaryDto;
import com.example.quiz_boot.modules.quiz.mapper.QuizMapper;
import com.example.quiz_boot.modules.quiz.service.QuizService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * Controller for managing quizzes.
 * Provides endpoints to retrieve quizzes with pagination, sorting, and
 * filtering
 * capabilities.
 * Supports filtering by categoryId or creatorId, but not both simultaneously.
 * Enforces a maximum page size of 100 to prevent excessive data retrieval.
 */
@RestController
@RequestMapping("/api/quizzes")
@Validated
public class QuizController {

  private final QuizService quizService;
  private final QuizMapper quizMapper;

  public QuizController(QuizService quizService, QuizMapper quizMapper) {
    this.quizService = quizService;
    this.quizMapper = quizMapper;
  }

  /**
   * Retrieves quizzes with pagination and optional filtering.
   *
   * @param pageable   pagination and sorting parameters
   * @param categoryId optional category filter
   * @param creatorId  optional creator filter
   * @return paginated list of quiz summaries
   */
  @GetMapping
  public ResponseEntity<Page<QuizSummaryDto>> getQuizzes(
      @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @RequestParam(required = false) @Positive Long categoryId,
      @RequestParam(required = false) @Positive Long creatorId) {

    // Enforce maximum page size for performance
    if (pageable.getPageSize() > 100) {
      pageable = PageRequest.of(pageable.getPageNumber(), 100, pageable.getSort());
    }

    // Validate sortable fields to prevent property access exceptions
    Set<String> allowedSorts = Set.of("id", "title", "createdAt", "updatedAt");
    for (Sort.Order order : pageable.getSort()) {
      if (!allowedSorts.contains(order.getProperty())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Invalid sort field: " + order.getProperty() + ". Allowed fields: " + allowedSorts);
      }
    }

    // Handle filtering scenarios - all return paginated results
    if (categoryId != null && creatorId != null) {
      // Reject combined filtering to keep API simple and clear
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Cannot filter by both categoryId and creatorId simultaneously. Please use one filter at a time.");
    }

    Page<QuizSummaryDto> result;
    if (categoryId != null) {
      // Filter by category with pagination
      result = quizService.getQuizzesByCategory(categoryId, pageable);
    } else if (creatorId != null) {
      // Filter by creator with pagination
      result = quizService.getQuizzesByCreator(creatorId, pageable);
    } else {
      // No filters - return all quizzes with pagination
      result = quizService.getAllQuizzes(pageable);
    }

    return ResponseEntity.ok(result);
  }

  /**
   * Creates a new quiz.
   *
   * @param quizCreateDto the quiz data to create
   * @return ResponseEntity with the created quiz summary and HTTP 201 status
   */
  @PostMapping
  public ResponseEntity<QuizSummaryDto> createQuiz(@RequestBody @Valid QuizCreateDto quizCreateDto) {
    System.out.println("DEBUG: Received QuizCreateDto: " + quizCreateDto);
    System.out.println("DEBUG: CategoryId value: " + quizCreateDto.getCategoryId());
    System.out.println("DEBUG: CategoryId type: "
        + (quizCreateDto.getCategoryId() != null ? quizCreateDto.getCategoryId().getClass().getSimpleName() : "null"));

    QuizResponseDto responseDto = quizService.createQuiz(quizCreateDto);

    // Convert QuizResponseDto to QuizSummaryDto using mapper
    QuizSummaryDto createdQuiz = quizMapper.toSummaryDto(responseDto);

    return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
  }

  /**
   * Gets a single quiz by ID.
   *
   * @param id the quiz ID
   * @return ResponseEntity with the quiz details
   */
  @GetMapping("/{id}")
  public ResponseEntity<QuizDetailDto> getQuizById(@PathVariable @Positive Long id) {
    QuizDetailDto quiz = quizService.getQuizById(id);
    return ResponseEntity.ok(quiz);
  }

  /**
   * Updates an existing quiz.
   *
   * @param id            the quiz ID to update
   * @param quizUpdateDto the updated quiz data
   * @return ResponseEntity with the updated quiz summary
   */
  @PutMapping("/{id}")
  public ResponseEntity<QuizSummaryDto> updateQuiz(
      @PathVariable @Positive Long id,
      @RequestBody @Valid QuizUpdateDto quizUpdateDto) {

    QuizResponseDto responseDto = quizService.updateQuiz(id, quizUpdateDto);
    QuizSummaryDto updatedQuiz = quizMapper.toSummaryDto(responseDto);

    return ResponseEntity.ok(updatedQuiz);
  }

  /**
   * Deletes a quiz by ID.
   *
   * @param id the quiz ID to delete
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuiz(@PathVariable @Positive Long id) {
    quizService.deleteQuiz(id);
    return ResponseEntity.noContent().build();
  }
}
