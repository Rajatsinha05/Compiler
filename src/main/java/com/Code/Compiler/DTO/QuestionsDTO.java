package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsDTO {
    private Long id;
    private String title;
    private String description;
    private String tags;
    private String difficultLevel;
    private String constraintValue;
    private String input;
    private String expectedOutput;
    private Long userId;
    private List<ExamplesDTO> examples;
}
