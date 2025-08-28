package com.example.quiz_boot.modules.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_boot.modules.quiz.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  Quiz findByTitle(String title);

  List<Quiz> findByCategoryId(Long categoryId);

  List<Quiz> findByActive(boolean active);

  List<Quiz> findByCreatedBy(Long userId);

  List<Quiz> findByIsPublished(boolean isPublished);

  List<Quiz> findByIsCreatorId(Long creatorId);

  List<Quiz> findByIsFeatured(boolean isFeatured);
}
