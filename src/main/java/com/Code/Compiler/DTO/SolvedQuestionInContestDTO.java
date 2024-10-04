package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolvedQuestionInContestDTO {
    private  Long id;
    private Long questionId;
    private Long contestId;
    private Long studentId;
    private Long contestQuestionId;
    private int obtainedMarks;
}
