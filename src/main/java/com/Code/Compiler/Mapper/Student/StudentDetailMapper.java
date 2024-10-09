//package com.Code.Compiler.Mapper.Student;
//
//import com.Code.Compiler.DTO.Student.*;
//import com.Code.Compiler.models.Students;
//
//import java.util.stream.Collectors;
//
//public class StudentDetailMapper {
//
//    public static StudentDetailDTO toDto(Students student) {
//        if (student == null) {
//            return null;
//        }
//
//        return new StudentDetailDTO(
//                student.getId(),
//                student.getName(),
//                student.getEmail(),
//                student.getGrid(),
//                student.getCourse(),
//                student.getBranchCode(),
//                student.getRole(),
//                // Enrolled Contests
//                student.getEnrolledContests().stream()
//                        .map(contest -> new ContestDTO(
//                                contest.getId(),
//                                contest.getTitle(),
//                                contest.getDescription(),
//                                contest.getDifficultyLevel()
//                        ))
//                        .collect(Collectors.toList()),
//                // Attempted Contests
//                student.getAttemptedContests().stream()
//                        .map(contest -> new ContestDTO(
//                                contest.getId(),
//                                contest.getTitle(),
//                                contest.getDescription(),
//                                contest.getDifficultyLevel()
//                        ))
//                        .collect(Collectors.toList()),
//                // Solved Questions
//                student.getSolvedQuestions().stream()
//                        .map(question -> new QuestionDTO(
//                                question.getId(),
//                                question.getTitle(),
//                                question.getDescription(),
//                                question.getTags(),
//                                question.getDifficultLevel().toString()
//                        ))
//                        .collect(Collectors.toList()),
//                // Solved Questions in Contest
//                student.getContestAttempts().stream()
//                        .flatMap(contestAttempt -> contestAttempt.getContest().getSolvedQuestions().stream()
//                                .filter(solvedQuestion -> solvedQuestion.getStudent().getId().equals(student.getId()))
//                                .map(solvedQuestion -> new SolvedQuestionInContestDTO(
//                                        solvedQuestion.getId(),
//                                        solvedQuestion.getQuestion().getId(),
//                                        solvedQuestion.getContest().getTitle(),
//                                        solvedQuestion.getObtainedMarks()
//                                )))
//                        .collect(Collectors.toList()),
//                // Contest Attempts
//                student.getContestAttempts().stream()
//                        .map(attempt -> new ContestAttemptDTO(
//                                attempt.getId(),
//                                attempt.getContest().getId(),
//                                attempt.getContest().getTitle(),
//                                attempt.getStartTime(),
//                                attempt.getEndTime(),
//                                attempt.getTotalMarks()
//                        ))
//                        .collect(Collectors.toList()),
//                // Ranking: This could be calculated or retrieved from a separate service
//                null // Optional ranking field can be set if available
//        );
//    }
//}
