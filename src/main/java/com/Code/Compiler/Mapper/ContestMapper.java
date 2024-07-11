package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class ContestMapper {
    public static ContestDTO toDTO(Contest contest) {
        List<Long> questionIds = contest.getQuestions() != null ? contest.getQuestions().stream().map(Questions::getId).collect(Collectors.toList()) : null;
        List<Long> enrolledStudentIds = contest.getEnrolledStudents() != null ? contest.getEnrolledStudents().stream().map(Students::getId).collect(Collectors.toList()) : null;
        return new ContestDTO(
                contest.getId(),
                contest.getTitle(),
                contest.getDescription(),
                contest.getStartTime(),
                contest.getEndTime(),
                contest.getTotalMarks(),
                contest.getDifficultyLevel(),
                contest.getCreatedBy() != null ? contest.getCreatedBy().getId() : null,
                questionIds,
                enrolledStudentIds
        );
    }

    public static Contest toEntity(ContestDTO contestDTO, User user, List<Questions> questions, List<Students> students) {
        Contest contest = new Contest();
        contest.setId(contestDTO.getId());
        contest.setTitle(contestDTO.getTitle());
        contest.setDescription(contestDTO.getDescription());
        contest.setStartTime(contestDTO.getStartTime());
        contest.setEndTime(contestDTO.getEndTime());
        contest.setTotalMarks(contestDTO.getTotalMarks());
        contest.setDifficultyLevel(contestDTO.getDifficultyLevel());
        contest.setCreatedBy(user);
        contest.setQuestions(questions);
        contest.setEnrolledStudents(students);
        return contest;
    }
}
