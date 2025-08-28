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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.quiz_boot.modules.quiz.dto.response.QuizSummaryDto;
import com.example.quiz_boot.modules.quiz.service.QuizService;

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

  public QuizController(QuizService quizService) {
    this.quizService = quizService;
  }

  /**
   * Retrieves a paginated list of quizzes with optional filtering and sorting.
   *
   * All endpoints return consistent paginated responses with metadata.
   * Supports filtering by categoryId OR creatorId (not both simultaneously).
   *
   * @param pageable   the pagination and sorting information (default: page=0,
   *                   size=10, sort=id,desc)
   * @param categoryId the ID of the category to filter by (optional, must be
   *                   positive)
   * @param creatorId  the ID of the creator to filter by (optional, must be
   *                   positive)
   * @return a ResponseEntity containing a Page of QuizSummaryDto with pagination
   *         metadata
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
}
