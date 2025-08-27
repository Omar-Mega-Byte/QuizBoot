package com.example.quiz_boot.modules.quiz.model;

import com.example.quiz_boot.modules.shared.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "question_options", indexes = {
    @jakarta.persistence.Index(name = "idx_option_question", columnList = "question_id"),
    @jakarta.persistence.Index(name = "idx_option_order", columnList = "option_order")
})
public class QuestionOption extends BaseEntity {

  @NotBlank
  @Size(min = 1, max = 200)
  @Column(nullable = false, length = 200)
  private String optionText;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id")
  private Question question;

  @Column(nullable = false)
  private boolean isCorrect = false;

  @Column(nullable = false)
  private int optionOrder;

  @Size(max = 500)
  @Column(length = 500)
  private String explanation;
}
