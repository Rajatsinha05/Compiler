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
    private Long createdBy;  // Modified to store just creator ID, previously createdById
    private List<ContestQuestionDTO> contestQuestions;  // Updated to include question details like title and marks
    private List<StudentDTO> enrolledStudents;  // To return student details, including only needed fields
}
