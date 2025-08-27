package com.example.quiz_boot.modules.quiz.model;

import java.util.List;

import com.example.quiz_boot.modules.shared.Base.BaseEntity;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "questions", indexes = {
    @jakarta.persistence.Index(name = "idx_question_quiz", columnList = "quiz_id"),
    @jakarta.persistence.Index(name = "idx_question_type", columnList = "question_type"),
    @jakarta.persistence.Index(name = "idx_question_order", columnList = "question_order")
})
public class Question extends BaseEntity {

  @NotBlank
  @Size(min = 10, max = 500)
  @Column(nullable = false, length = 500)
  private String questionText;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "quiz_id")
  private Quiz quiz;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<QuestionOption> options;

  @Column(nullable = false)
  private String questionType; // MULTIPLE_CHOICE, TRUE_FALSE, FILL_IN_BLANK

  @Min(1)
  @Max(100)
  @Column(nullable = false)
  private int questionOrder;

  @Min(1)
  @Max(10)
  @Column(nullable = false)
  private int points;

  @Size(max = 1000)
  @Column(length = 1000)
  private String explanation;

  @Column(nullable = false)
  private boolean isRequired = true;
}
