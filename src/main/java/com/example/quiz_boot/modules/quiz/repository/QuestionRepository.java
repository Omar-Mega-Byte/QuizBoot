package com.example.quiz_boot.modules.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_boot.modules.quiz.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  Question findByQuestionText(String questionText);

  List<Question> findByQuizId(Long quizId);
}
