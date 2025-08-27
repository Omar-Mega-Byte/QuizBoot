package com.example.quiz_boot.modules.quiz.model;

import com.example.quiz_boot.modules.shared.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "categories")
public class Category extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @Column(length = 1000)
  private String description;
}
