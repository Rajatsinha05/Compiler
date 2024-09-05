package com.Code.Compiler.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSolvedQuestionsDTO {
    private String studentName;
    private List<QuestionDTO> solvedQuestions;



    // Getters and setters...
}
