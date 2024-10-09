package com.Code.Compiler.DTO.Student;


import com.Code.Compiler.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailDTO {
    // Basic Information
    private Long id;
    private String name;
    private String email;
    private String grid;
    private String course;
    private String branchCode;
    private Role role;

    // Contests Information
    private List<ContestDTO> enrolledContests; // Contests in which the student is enrolled
    private List<ContestDTO> attemptedContests; // Contests attempted by the student

    // Solved Questions
    private List<QuestionDTO> solvedQuestions; // Questions solved by the student
    private List<SolvedQuestionInContestDTO> solvedQuestionsInContest; // Questions solved by the student in contests

    // Contest Attempts
    private List<ContestAttemptDTO> contestAttempts; // Student's attempts in contests

    // Ranking (optional if available)
    private Integer ranking; // The student's ranking, if available
}
