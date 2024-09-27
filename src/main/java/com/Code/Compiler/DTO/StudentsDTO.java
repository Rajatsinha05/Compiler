package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentsDTO {
    private Long id;
    private String name;
    private String email;
    private String grid;
    private String course;
    private String branchCode;
    private String role;
    private List<String> solvedQuestionTitles;  // Optionally you can include solved questions
}
