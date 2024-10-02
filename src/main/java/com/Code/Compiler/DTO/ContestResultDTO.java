package com.Code.Compiler.DTO;

import java.util.List;

public class ContestResultDTO {
    private Long contestId;
    private Long studentId;
    private List<SolvedQuestionInContestDTO> solvedQuestions;

    // Getters and Setters
    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public List<SolvedQuestionInContestDTO> getSolvedQuestions() {
        return solvedQuestions;
    }

    public void setSolvedQuestions(List<SolvedQuestionInContestDTO> solvedQuestions) {
        this.solvedQuestions = solvedQuestions;
    }
}
