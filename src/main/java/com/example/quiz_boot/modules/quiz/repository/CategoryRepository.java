package com.example.quiz_boot.modules.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_boot.modules.quiz.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Category findByName(String name);

  List<Category> findAllByIdIn(List<Long> ids);
}
