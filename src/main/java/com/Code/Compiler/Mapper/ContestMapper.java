package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.DTO.ContestQuestionDTO;
import com.Code.Compiler.DTO.QuestionDTO;
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

        ContestDTO dto = new ContestDTO();
        dto.setId(contest.getId());
        dto.setTitle(contest.getTitle());
        dto.setDescription(contest.getDescription());
        dto.setStartTime(contest.getStartTime());
        dto.setEndTime(contest.getEndTime());
        dto.setTotalMarks(contest.getTotalMarks());
        dto.setDifficultyLevel(contest.getDifficultyLevel());

        // Safely map createdBy
        if (contest.getCreatedBy() != null) {
            dto.setCreatedById(contest.getCreatedBy().getId());
        } else {
            log.warn("Contest with ID: {} has no creator assigned", contest.getId());
        }

        // Map contest questions
        if (contest.getContestQuestions() != null) {
            dto.setQuestions(contest.getContestQuestions().stream()
                    .map(q -> new QuestionDTO(q.getQuestion().getId(), q.getQuestion().getTitle()))
                    .collect(Collectors.toList()));
        } else {
            dto.setQuestions(Collections.emptyList());
        }

        // Map enrolled students
        if (contest.getEnrolledStudents() != null) {
            dto.setEnrolledStudents(contest.getEnrolledStudents().stream()
                    .map(ContestMapper::studentToDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setEnrolledStudents(Collections.emptyList());
        }

        log.info("Mapped Contest entity with ID: {} to ContestDTO", contest.getId());

        return dto;
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
        contest.setDifficultyLevel(contestDTO.getDifficultyLevel());

        // Set the created by user (null-safe)
        if (user != null) {
            contest.setCreatedBy(user);
        } else {
            log.warn("User is null while mapping ContestDTO to Contest entity");
        }

        // Convert List<Questions> to List<ContestQuestion>
        if (questions != null) {
            List<ContestQuestion> contestQuestions = questions.stream()
                    .map(question -> new ContestQuestion(null, contest, question, 0)) // Assuming marks are 0 by default, you can modify this as needed
                    .collect(Collectors.toList());
            contest.setContestQuestions(contestQuestions);
        } else {
            contest.setContestQuestions(Collections.emptyList());
        }

        // Set enrolled students
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
                student.getEmail(),
                student.getGrid(),
                student.getCourse(),
                student.getBranchCode()
        );
    }
}
