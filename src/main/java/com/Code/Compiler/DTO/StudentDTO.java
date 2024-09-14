package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;          // The student's ID
    private String name;      // The student's name
    private String email;     // The student's email address
    private String grid;      // The student's grid
    private String course;    // The student's course
    private String branchCode; // The student's branch code
    // Add any additional fields as needed
}

