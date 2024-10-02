package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionScoreDTO {
    private Long contestQuestionId; // The ContestQuestionID to link with
    private int marksObtained; // Marks obtained for the question
}
