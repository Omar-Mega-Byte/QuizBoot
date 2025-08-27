package com.example.quiz_boot.modules.quiz.model;

import java.util.List;

import com.example.quiz_boot.modules.shared.Base.BaseEntity;
import com.example.quiz_boot.modules.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "quizzes", indexes = {
    @jakarta.persistence.Index(name = "idx_quiz_title", columnList = "title"),
    @jakarta.persistence.Index(name = "idx_quiz_category", columnList = "category_id"),
    @jakarta.persistence.Index(name = "idx_quiz_creator", columnList = "creator_id")
})
public class Quiz extends BaseEntity {
  @Column(nullable = false)
  private String title;

  @Column(length = 1000, nullable = false)
  private String description;

  @ManyToOne(optional = false, fetch = jakarta.persistence.FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne(optional = false, fetch = jakarta.persistence.FetchType.LAZY)
  @JoinColumn(name = "creator_id")
  private User creator;

  @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Question> questions;

  @Min(0)
  @Max(3)
  @Column(nullable = false)
  private int duration;

  @Min(0)
  @Max(100)
  @Column(nullable = false)
  private double passingScore;

  @Min(1)
  @Max(10)
  @Column(nullable = false)
  private int maxAttempts;
}
