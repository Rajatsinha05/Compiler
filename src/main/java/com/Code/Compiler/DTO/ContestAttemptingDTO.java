package com.Code.Compiler.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestAttemptingDTO {
    private Long id;
    private Long contestId;
    private Long studentId;
    private Integer obtainMarks;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String username; // Student's name
    private String email;    // Student's email
    private int totalMarks;  // Contest's total marks
}
