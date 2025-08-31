package com.example.quiz_boot.modules.quiz.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.quiz_boot.modules.quiz.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

  // Find by title
  Quiz findByTitle(String title);

  // Find by category
  List<Quiz> findByCategoryId(Long categoryId);

  Page<Quiz> findByCategoryId(Long categoryId, Pageable pageable);

  // Find by creator
  List<Quiz> findByCreatorId(Long creatorId);

  Page<Quiz> findByCreatorId(Long creatorId, Pageable pageable);

  // Custom query to search by title containing
  @Query("SELECT q FROM Quiz q WHERE q.title LIKE %:title%")
  Page<Quiz> findByTitleContaining(@Param("title") String title, Pageable pageable);

  // Complex search query
  @Query("SELECT q FROM Quiz q WHERE " +
      "(:title IS NULL OR q.title LIKE %:title%) AND " +
      "(:categoryId IS NULL OR q.category.id = :categoryId) AND " +
      "(:creatorId IS NULL OR q.creator.id = :creatorId)")
  Page<Quiz> findWithFilters(@Param("title") String title,
      @Param("categoryId") Long categoryId,
      @Param("creatorId") Long creatorId,
      Pageable pageable);
}
