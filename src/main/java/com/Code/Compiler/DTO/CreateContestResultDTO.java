package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContestResultDTO {
    private Long contestId;
    private Long studentId;
    private int totalScore;
    private List<QuestionScoreDTO> questionScores; // List of scores for each question
}
