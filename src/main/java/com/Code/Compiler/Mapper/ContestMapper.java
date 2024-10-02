package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.DTO.ContestQuestionDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.models.*;
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

        // Map contest questions to ContestQuestionDTOs
        List<ContestQuestionDTO> contestQuestions = Optional.ofNullable(contest.getContestQuestions())
                .orElse(Collections.emptyList()).stream()
                .map(ContestMapper::contestQuestionToDTO)
                .collect(Collectors.toList());

        // Map enrolled students to StudentDTOs
        List<StudentDTO> enrolledStudents = Optional.ofNullable(contest.getEnrolledStudents())
                .orElse(Collections.emptyList()).stream()
                .map(ContestMapper::studentToDTO)
                .collect(Collectors.toList());

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
                contestQuestions,
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

    // Helper method to convert ContestQuestion entity to ContestQuestionDTO
    private static ContestQuestionDTO contestQuestionToDTO(ContestQuestion contestQuestion) {
        if (contestQuestion == null) {
            log.error("ContestQuestion entity is null, cannot map to ContestQuestionDTO");
            return null;
        }

        return new ContestQuestionDTO(
                contestQuestion.getQuestion().getId(),
                contestQuestion.getMarks(),
                contestQuestion.getQuestion().getTitle() // Assuming the question entity has a title field

        );
    }

    // Helper method to convert Students entity to StudentDTO
    public static StudentDTO studentToDTO(Students student) {
        if (student == null) {
            log.error("Students entity is null, cannot map to StudentDTO");
            return null;
        }

        return new StudentDTO(
                student.getId(),
                student.getName(),  // Can be null if not available
                student.getEmail()  // Can be null if not available
        );
    }
}
