package com.Code.Compiler.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ContestQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int marks;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private Questions questions;

  @ManyToOne
  @JoinColumn(name = "contest_id")
  private Contest contest;
}
