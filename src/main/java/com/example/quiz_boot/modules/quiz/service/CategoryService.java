package com.example.quiz_boot.modules.quiz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.quiz_boot.modules.quiz.dto.request.CategoryCreateDto;
import com.example.quiz_boot.modules.quiz.dto.request.CategoryUpdateDto;
import com.example.quiz_boot.modules.quiz.dto.response.CategoryResponseDto;
import com.example.quiz_boot.modules.quiz.dto.response.CategorySummaryDto;
import com.example.quiz_boot.modules.quiz.exception.CategoryNotFoundException;
import com.example.quiz_boot.modules.quiz.exception.InvalidCategoryException;
import com.example.quiz_boot.modules.quiz.mapper.CategoryMapper;
import com.example.quiz_boot.modules.quiz.model.Category;
import com.example.quiz_boot.modules.quiz.repository.CategoryRepository;
import com.example.quiz_boot.modules.quiz.validation.CategoryValidation;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
  private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

  private final CategoryRepository categoryRepository;
  private final CategoryValidation categoryValidation;
  private final CategoryMapper categoryMapper;

  public CategoryService(CategoryRepository categoryRepository,
      CategoryValidation categoryValidation,
      CategoryMapper categoryMapper) {
    this.categoryRepository = categoryRepository;
    this.categoryValidation = categoryValidation;
    this.categoryMapper = categoryMapper;
  }

  @Transactional
  public CategoryResponseDto createCategory(CategoryCreateDto categoryCreateDto) {
    // Audit: Log category creation
    logger.info("AUDIT: Creating category: {}",
        categoryCreateDto != null ? categoryCreateDto.getName() : "null");

    if (categoryCreateDto == null) {
      logger.warn("AUDIT: Category creation failed: CategoryCreateDto is null");
      throw new InvalidCategoryException("Category creation data cannot be null");
    }

    if (!categoryValidation.isValidForCreation(
        categoryCreateDto.getName(),
        categoryCreateDto.getDescription())) {
      logger.warn("AUDIT: Category creation failed: Invalid category data: {}", categoryCreateDto);
      throw new InvalidCategoryException("Invalid category data provided");
    }

    try {
      // Create normalized category
      CategoryCreateDto normalizedCategory = new CategoryCreateDto();
      normalizedCategory.setName(categoryCreateDto.getName().trim());
      normalizedCategory.setDescription(
          categoryCreateDto.getDescription() != null ? categoryCreateDto.getDescription().trim() : null);

      // Convert to entity and save
      Category savedCategory = categoryRepository.save(categoryMapper.toEntity(normalizedCategory));

      // Audit: Log successful category creation
      logger.info("AUDIT: Category created successfully with ID: {}", savedCategory.getId());

      return categoryMapper.toResponseDto(savedCategory);
    } catch (Exception e) {
      logger.error("AUDIT: Category creation failed due to database error: {}", e.getMessage());
      throw new InvalidCategoryException("Failed to create category due to database error", e);
    }
  }

  @Transactional
  public CategoryResponseDto updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) {
    logger.info("AUDIT: Updating category with ID: {}", id);

    if (id == null || id <= 0) {
      throw new InvalidCategoryException("Category ID must be valid");
    }

    if (categoryUpdateDto == null) {
      logger.warn("AUDIT: Category update failed: CategoryUpdateDto is null");
      throw new InvalidCategoryException("Category update data cannot be null");
    }

    if (!categoryValidation.isValidForUpdate(
        categoryUpdateDto.getName(),
        categoryUpdateDto.getDescription())) {
      logger.warn("AUDIT: Category update failed: Invalid category data: {}", categoryUpdateDto);
      throw new InvalidCategoryException("Invalid category data provided");
    }

    try {
      Category existingCategory = categoryRepository.findById(id)
          .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

      // Create normalized update
      CategoryUpdateDto normalizedUpdate = new CategoryUpdateDto();
      normalizedUpdate.setName(categoryUpdateDto.getName().trim());
      normalizedUpdate.setDescription(
          categoryUpdateDto.getDescription() != null ? categoryUpdateDto.getDescription().trim() : null);

      // Update entity
      categoryMapper.updateEntity(existingCategory, normalizedUpdate);
      Category updatedCategory = categoryRepository.save(existingCategory);

      logger.info("AUDIT: Category updated successfully with ID: {}", updatedCategory.getId());

      return categoryMapper.toResponseDto(updatedCategory);
    } catch (CategoryNotFoundException e) {
      logger.warn("AUDIT: Category update failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("AUDIT: Category update failed due to database error: {}", e.getMessage());
      throw new InvalidCategoryException("Failed to update category due to database error", e);
    }
  }

  public CategoryResponseDto getCategoryById(Long id) {
    logger.debug("Retrieving category with ID: {}", id);

    if (id == null || id <= 0) {
      throw new InvalidCategoryException("Category ID must be valid");
    }

    try {
      Category category = categoryRepository.findById(id)
          .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

      return categoryMapper.toResponseDto(category);
    } catch (CategoryNotFoundException e) {
      logger.warn("Category retrieval failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("AUDIT: Category retrieval failed due to database error: {}", e.getMessage());
      throw new InvalidCategoryException("Failed to retrieve category due to database error", e);
    }
  }

  public List<CategorySummaryDto> getAllCategories() {
    logger.debug("Retrieving all categories");

    try {
      List<Category> categories = categoryRepository.findAll();
      return categoryMapper.toSummaryDtoList(categories);
    } catch (Exception e) {
      logger.error("AUDIT: Categories retrieval failed due to database error: {}", e.getMessage());
      throw new InvalidCategoryException("Failed to retrieve categories due to database error", e);
    }
  }

  public Page<CategorySummaryDto> getAllCategories(Pageable pageable) {
    logger.debug("Retrieving categories with pagination: {}", pageable);

    try {
      Page<Category> categories = categoryRepository.findAll(pageable);
      return categories.map(categoryMapper::toSummaryDto);
    } catch (Exception e) {
      logger.error("AUDIT: Categories retrieval failed due to database error: {}", e.getMessage());
      throw new InvalidCategoryException("Failed to retrieve categories due to database error", e);
    }
  }

  @Transactional
  public void deleteCategory(Long id) {
    logger.info("AUDIT: Deleting category with ID: {}", id);

    if (id == null || id <= 0) {
      throw new InvalidCategoryException("Category ID must be valid");
    }

    try {
      if (!categoryRepository.existsById(id)) {
        throw new CategoryNotFoundException("Category not found with ID: " + id);
      }

      categoryRepository.deleteById(id);
      logger.info("AUDIT: Category deleted successfully with ID: {}", id);
    } catch (CategoryNotFoundException e) {
      logger.warn("AUDIT: Category deletion failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("AUDIT: Category deletion failed due to database error: {}", e.getMessage());
      throw new InvalidCategoryException("Failed to delete category due to database error", e);
    }
  }

  public boolean existsById(Long id) {
    if (id == null || id <= 0) {
      return false;
    }

    try {
      return categoryRepository.existsById(id);
    } catch (Exception e) {
      logger.error("Error checking category existence: {}", e.getMessage());
      return false;
    }
  }
}
