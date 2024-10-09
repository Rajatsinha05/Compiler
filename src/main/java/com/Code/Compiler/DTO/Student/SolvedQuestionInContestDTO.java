package com.Code.Compiler.DTO.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolvedQuestionInContestDTO {
    private Long id;
    private Long questionId;
    private String contestTitle;
    private int obtainedMarks;
}