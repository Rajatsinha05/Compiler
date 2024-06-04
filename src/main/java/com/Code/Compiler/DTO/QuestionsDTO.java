package com.Code.Compiler.DTO;


import com.Code.Compiler.Enum.DifficultLevel;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.List;
@Setter
@Getter
@ToString
@Transactional
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsDTO {
    private Long id;
    private String title;
    private String description;
    private DifficultLevel difficultLevel;
    private String constraintValue;
    private String input;
    private List<String> expectedOutput;
    private UserDTO user;
}

