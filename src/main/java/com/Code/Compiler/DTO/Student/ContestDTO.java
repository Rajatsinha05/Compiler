package com.Code.Compiler.DTO.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestDTO {
    private Long id;
    private String title;
    private String description;
    private String difficultyLevel;
}
