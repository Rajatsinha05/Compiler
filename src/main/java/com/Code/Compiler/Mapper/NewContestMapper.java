package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestQuestionDTO;
import com.Code.Compiler.DTO.NewContestDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewContestMapper {

    private final UserRepository userRepository;

    // Maps DTO to Entity
    public Contest toEntity(NewContestDTO contestDTO) {
        if (contestDTO == null) {
            log.error("ContestDTO is null, cannot map to Contest entity");
            return null;
        }

        Contest contest = new Contest();
        contest.setTitle(contestDTO.getTitle());
        contest.setDescription(contestDTO.getDescription());
        contest.setStartTime(contestDTO.getStartTime());
        contest.setEndTime(contestDTO.getEndTime());
        contest.setTotalMarks(contestDTO.getTotalMarks());
        contest.setDifficultyLevel(contestDTO.getDifficultyLevel());

        // Set creator user, log a warning if not found
        Optional<User> userOpt = userRepository.findById(contestDTO.getCreatedBy());
        if (userOpt.isPresent()) {
            contest.setCreatedBy(userOpt.get());
        } else {
            log.warn("User not found with ID: {}, cannot set as contest creator", contestDTO.getCreatedBy());
        }

        // Set contest questions without deep references to the contest itself
        contest.setContestQuestions(
                Optional.ofNullable(contestDTO.getContestQuestions()).orElse(Collections.emptyList())
                        .stream()
                        .map(dto -> {
                            Questions question = new Questions();
                            question.setId(dto.getQuestionId());
                            return new ContestQuestion(null, question, dto.getMarks(), contest);
                        })
                        .collect(Collectors.toList())
        );

        // Set enrolled students without deep references
        contest.setEnrolledStudents(
                Optional.ofNullable(contestDTO.getEnrolledStudents()).orElse(Collections.emptyList())
                        .stream()
                        .map(dto -> {
                            Students student = new Students();
                            student.setId(dto.getId());
                            return student;
                        })
                        .collect(Collectors.toList())
        );

        return contest;
    }

    // Maps Entity to DTO
    public NewContestDTO toDTO(Contest contest) {
        if (contest == null) {
            log.error("Contest entity is null, cannot map to NewContestDTO");
            return null;
        }

        return new NewContestDTO(
                contest.getId(),
                contest.getTitle(),
                contest.getDescription(),
                contest.getStartTime(),
                contest.getEndTime(),
                contest.getTotalMarks(),
                contest.getDifficultyLevel(),
                contest.getCreatedBy() != null ? contest.getCreatedBy().getId() : null,
                Optional.ofNullable(contest.getContestQuestions()).orElse(Collections.emptyList())
                        .stream()
                        .map(question -> new ContestQuestionDTO(question.getQuestion().getId(), question.getMarks(),null))
                        .collect(Collectors.toList()),
                Optional.ofNullable(contest.getEnrolledStudents()).orElse(Collections.emptyList())
                        .stream()
                        .map(student -> new StudentDTO(student.getId(), student.getName(), student.getEmail()))
                        .collect(Collectors.toList())
        );
    }
}
