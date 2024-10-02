package com.Code.Compiler.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    @JsonBackReference // Prevents recursion during serialization (parent side)
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Students student;

    // Stores each solved question in the contest, along with its score
    @OneToMany(mappedBy = "contestResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Manages the child relationship
    private List<SolvedQuestionInContest> solvedQuestions;

    // Helper method to calculate the total score from solved questions
    public int getTotalScore() {
        return solvedQuestions.stream().mapToInt(SolvedQuestionInContest::getObtainedMarks).sum();
    }
}
