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
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_boot.modules.quiz.dto.request.CategoryCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.CategoryUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.CategoryResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.CategorySummaryDto;
import com.example.quiz_boot.modules.quiz.service.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * Controller for managing categories.
 */
@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  /**
   * Gets all categories with pagination.
   *
   * @param pageable pagination parameters
   * @return paginated list of categories
   */
  @GetMapping
  public ResponseEntity<Page<CategorySummaryDto>> getCategories(
      @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    // Enforce maximum page size
    if (pageable.getPageSize() > 100) {
      pageable = PageRequest.of(pageable.getPageNumber(), 100, pageable.getSort());
    }

    Page<CategorySummaryDto> result = categoryService.getAllCategories(pageable);
    return ResponseEntity.ok(result);
  }

  /**
   * Gets all categories without pagination (for dropdowns).
   *
   * @return list of all categories
   */
  @GetMapping("/all")
  public ResponseEntity<java.util.List<CategorySummaryDto>> getAllCategories() {
    java.util.List<CategorySummaryDto> result = categoryService.getAllCategories();
    return ResponseEntity.ok(result);
  }

  /**
   * Creates a new category.
   *
   * @param categoryCreateDto the category data to create
   * @return ResponseEntity with the created category and HTTP 201 status
   */
  @PostMapping
  public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody @Valid CategoryCreateDto categoryCreateDto) {
    CategoryResponseDto createdCategory = categoryService.createCategory(categoryCreateDto);
    return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
  }

  /**
   * Gets a single category by ID.
   *
   * @param id the category ID
   * @return ResponseEntity with the category details
   */
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable @Positive Long id) {
    CategoryResponseDto category = categoryService.getCategoryById(id);
    return ResponseEntity.ok(category);
  }

  /**
   * Updates an existing category.
   *
   * @param id                the category ID to update
   * @param categoryUpdateDto the updated category data
   * @return ResponseEntity with the updated category
   */
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> updateCategory(
      @PathVariable @Positive Long id,
      @RequestBody @Valid CategoryUpdateDto categoryUpdateDto) {

    CategoryResponseDto updatedCategory = categoryService.updateCategory(id, categoryUpdateDto);
    return ResponseEntity.ok(updatedCategory);
  }

  /**
   * Deletes a category by ID.
   *
   * @param id the category ID to delete
   * @return ResponseEntity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable @Positive Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
