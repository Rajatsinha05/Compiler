package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.DTO.QuestionDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContestMapper {

    private static final Logger log = LoggerFactory.getLogger(ContestMapper.class);

    // Convert Contest entity to ContestDTO
    public static ContestDTO toDTO(Contest contest) {
        if (contest == null) {
            log.error("Contest entity is null, cannot map to ContestDTO");
            return null;
        }

        // Map question IDs and QuestionDTOs (containing only id and title)
        List<Long> questionIds = Optional.ofNullable(contest.getQuestions())
                .orElse(Collections.emptyList()).stream().map(Questions::getId).collect(Collectors.toList());
        List<QuestionDTO> questions = Optional.ofNullable(contest.getQuestions())
                .orElse(Collections.emptyList()).stream()
                .map(question -> new QuestionDTO(question.getId(), question.getTitle())) // Map only id and title
                .collect(Collectors.toList());

        // Map enrolled student IDs and StudentDTOs using Optional
        List<Long> enrolledStudentIds = Optional.ofNullable(contest.getEnrolledStudents())
                .orElse(Collections.emptyList()).stream().map(Students::getId).collect(Collectors.toList());
        List<StudentDTO> enrolledStudents = Optional.ofNullable(contest.getEnrolledStudents())
                .orElse(Collections.emptyList()).stream().map(ContestMapper::studentToDTO).collect(Collectors.toList());

        log.info("Mapped Contest entity with ID: {} to ContestDTO", contest.getId());

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
                questions,  // Return only id and title of questions
                enrolledStudentIds,
                enrolledStudents
        );
    }

    // Convert ContestDTO to Contest entity
    public static Contest toEntity(ContestDTO contestDTO, User user, List<Questions> questions, List<Students> students) {
        if (contestDTO == null) {
            log.error("ContestDTO is null, cannot map to Contest entity");
            return null;
        }

        Contest contest = new Contest();
        contest.setId(contestDTO.getId());
        contest.setTitle(contestDTO.getTitle());
        contest.setDescription(contestDTO.getDescription());
        contest.setStartTime(contestDTO.getStartTime());
        contest.setEndTime(contestDTO.getEndTime());
        contest.setTotalMarks(contestDTO.getTotalMarks());

        // Set the difficulty level (null-safe)
        contest.setDifficultyLevel(contestDTO.getDifficultyLevel() != null ?
                contestDTO.getDifficultyLevel() : null);

        // Set the created by user (null-safe)
        if (user != null) {
            contest.setCreatedBy(user);
        } else {
            log.warn("User is null while mapping ContestDTO to Contest entity");
        }

        // Set questions and enrolled students
        contest.setQuestions(questions != null ? questions : Collections.emptyList());
        contest.setEnrolledStudents(students != null ? students : Collections.emptyList());

        log.info("Mapped ContestDTO with ID: {} to Contest entity", contestDTO.getId());

        return contest;
    }

    // Helper method to convert Students entity to StudentDTO
    public static StudentDTO studentToDTO(Students student) {
        if (student == null) {
            log.error("Students entity is null, cannot map to StudentDTO");
            return null;
        }

        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
    }
}
