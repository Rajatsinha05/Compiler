package com.Code.Compiler.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolvedQuestionInContest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Questions question;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    @JsonBackReference // Prevents recursion when serializing contest relationship
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "contest_question_id", nullable = false)
    private ContestQuestion contestQuestion;

    @ManyToOne
    @JoinColumn(name = "contest_result_id")
    @JsonManagedReference // Manages the relationship with ContestResult
    private ContestResult contestResult;

    private int obtainedMarks;
}
