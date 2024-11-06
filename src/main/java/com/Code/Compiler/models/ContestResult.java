package com.Code.Compiler.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ContestResult {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Students student;

    // Stores each solved question in the contest, along with its score
    @OneToMany(mappedBy = "contestResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SolvedQuestionInContest> solvedQuestions;

    // Helper method to calculate the total score from solved questions
    public int getTotalScore() {
        return solvedQuestions.stream().mapToInt(SolvedQuestionInContest::getObtainedMarks).sum();
    }
}
