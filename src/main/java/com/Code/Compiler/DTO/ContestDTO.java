package com.Code.Compiler.DTO;

import com.Code.Compiler.Enum.DifficultLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalMarks;
    private DifficultLevel difficultyLevel;
    private Long createdById;
    private List<Long> questionIds;  // Optional: if you want only question IDs
    private List<QuestionDTO> questions;  // For returning question details
    private List<Long> enrolledStudentIds;  // Optional: if you want only student IDs
    private List<StudentDTO> enrolledStudents;  // For returning student details
}
