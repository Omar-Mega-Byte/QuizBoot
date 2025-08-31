package com.example.quiz_boot.modules.quiz.controller;

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

import com.example.quiz_boot.modules.quiz.dto.request.QuestionCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.QuestionUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuestionResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.QuestionSummaryDto;
import com.example.quiz_boot.modules.quiz.service.QuestionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * Controller for managing questions.
 */
@RestController
@RequestMapping("/api/questions")
@Validated
public class QuestionController {

  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  /**
   * Gets questions by quiz ID with pagination.
   *
   * @param quizId   the quiz ID to filter by
   * @param pageable pagination parameters
   * @return paginated list of questions
   */
  @GetMapping
  public ResponseEntity<Page<QuestionSummaryDto>> getQuestionsByQuizId(
      @RequestParam @Positive Long quizId,
      @PageableDefault(page = 0, size = 10, sort = "questionOrder", direction = Sort.Direction.ASC) Pageable pageable) {

    // Enforce maximum page size
    if (pageable.getPageSize() > 100) {
      pageable = PageRequest.of(pageable.getPageNumber(), 100, pageable.getSort());
    }

    Page<QuestionSummaryDto> result = questionService.getQuestionsByQuizId(quizId, pageable);
    return ResponseEntity.ok(result);
  }

  /**
   * Creates a new question.
   *
   * @param questionCreateDto the question data to create
   * @return ResponseEntity with the created question and HTTP 201 status
   */
  @PostMapping
  public ResponseEntity<QuestionResponseDto> createQuestion(@RequestBody @Valid QuestionCreateDto questionCreateDto) {
    QuestionResponseDto createdQuestion = questionService.createQuestion(questionCreateDto);
    return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
  }

  /**
   * Gets a single question by ID.
   *
   * @param id the question ID
   * @return ResponseEntity with the question details
   */
  @GetMapping("/{id}")
  public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable @Positive Long id) {
    QuestionResponseDto question = questionService.getQuestionById(id);
    return ResponseEntity.ok(question);
  }

  /**
   * Updates an existing question.
   *
   * @param id                the question ID to update
   * @param questionUpdateDto the updated question data
   * @return ResponseEntity with the updated question
   */
  @PutMapping("/{id}")
  public ResponseEntity<QuestionResponseDto> updateQuestion(
      @PathVariable @Positive Long id,
      @RequestBody @Valid QuestionUpdateDto questionUpdateDto) {

    QuestionResponseDto updatedQuestion = questionService.updateQuestion(id, questionUpdateDto);
    return ResponseEntity.ok(updatedQuestion);
  }

  /**
   * Deletes a question by ID.
   *
   * @param id the question ID to delete
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuestion(@PathVariable @Positive Long id) {
    questionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }
}
