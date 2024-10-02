package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestResultDTO;
import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class ContestResultMapper {

    public static ContestResultDTO toDTO(ContestResult contestResult) {
        ContestResultDTO dto = new ContestResultDTO();
        dto.setContestId(contestResult.getContest().getId());
        dto.setStudentId(contestResult.getStudent().getId());
        dto.setSolvedQuestions(contestResult.getSolvedQuestions().stream()
                .map(ContestResultMapper::toSolvedQuestionInContestDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static ContestResult toEntity(ContestResultDTO dto, Contest contest, Students student) {
        ContestResult contestResult = new ContestResult();
        contestResult.setContest(contest);
        contestResult.setStudent(student);

        List<SolvedQuestionInContest> solvedQuestions = dto.getSolvedQuestions().stream()
                .map(solvedDTO -> toSolvedQuestionInContestEntity(solvedDTO))
                .collect(Collectors.toList());

        solvedQuestions.forEach(solvedQuestion -> solvedQuestion.setContest(contest));  // Set contest reference
        solvedQuestions.forEach(solvedQuestion -> solvedQuestion.setStudent(student));  // Set student reference
        contestResult.setSolvedQuestions(solvedQuestions);

        return contestResult;
    }

    public static SolvedQuestionInContestDTO toSolvedQuestionInContestDTO(SolvedQuestionInContest solvedQuestion) {
        SolvedQuestionInContestDTO dto = new SolvedQuestionInContestDTO();
        dto.setQuestionId(solvedQuestion.getQuestion().getId());
        dto.setContestId(solvedQuestion.getContest().getId());
        dto.setStudentId(solvedQuestion.getStudent().getId());
        dto.setContestQuestionId(solvedQuestion.getContestQuestion().getId());
        dto.setObtainedMarks(solvedQuestion.getObtainedMarks());
        return dto;
    }

    public static SolvedQuestionInContest toSolvedQuestionInContestEntity(SolvedQuestionInContestDTO dto) {
        SolvedQuestionInContest solvedQuestion = new SolvedQuestionInContest();

        // Set the question reference
        Questions question = new Questions();
        question.setId(dto.getQuestionId());
        solvedQuestion.setQuestion(question);

        // Set the contest reference
        Contest contest = new Contest();
        contest.setId(dto.getContestId());
        solvedQuestion.setContest(contest);

        // Set the student reference
        Students student = new Students();
        student.setId(dto.getStudentId());
        solvedQuestion.setStudent(student);

        // Set the contest question reference
        ContestQuestion contestQuestion = new ContestQuestion();
        contestQuestion.setId(dto.getContestQuestionId());
        solvedQuestion.setContestQuestion(contestQuestion);

        // Set obtained marks
        solvedQuestion.setObtainedMarks(dto.getObtainedMarks());

        return solvedQuestion;
    }
}
