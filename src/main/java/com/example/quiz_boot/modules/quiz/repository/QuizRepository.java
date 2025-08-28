package com.example.quiz_boot.modules.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_boot.modules.quiz.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
  Quiz findByTitle(String title);

  List<Quiz> findByCategoryId(Long categoryId);
}
