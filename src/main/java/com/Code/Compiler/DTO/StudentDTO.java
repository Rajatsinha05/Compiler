package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;        // The student's ID
    private String name;     // The student's name
    private String email;    // The student's email address
    
}

