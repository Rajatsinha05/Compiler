package com.Code.Compiler.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestQuestionDTO {
    private Long questionId;
    private String questionTitle;
    private int marks;
}
