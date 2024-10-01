package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestQuestionDTO;
import com.Code.Compiler.DTO.NewContestDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.ContestQuestion;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.Students;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component  // Ensure this class is a Spring bean
@RequiredArgsConstructor
@Slf4j
public class NewContestMapper {

    private final UserRepository userRepository;

    // Maps DTO to Entity
    public Contest toEntity(NewContestDTO contestDTO) {
        Contest contest = new Contest();
        contest.setTitle(contestDTO.getTitle());
        contest.setDescription(contestDTO.getDescription());
        contest.setStartTime(contestDTO.getStartTime());
        contest.setEndTime(contestDTO.getEndTime());
        contest.setTotalMarks(contestDTO.getTotalMarks());
        contest.setDifficultyLevel(contestDTO.getDifficultyLevel());

        // Set creator user
        userRepository.findById(contestDTO.getCreatedBy()).ifPresent(contest::setCreatedBy);

        // Set contest questions without deep references to the contest itself
        contest.setContestQuestions(
                contestDTO.getContestQuestions().stream()
                        .map(dto -> {
                            Questions question = new Questions();
                            question.setId(dto.getQuestionId());
                            return new ContestQuestion(null, question, dto.getMarks(), contest);
                        })
                        .collect(Collectors.toList())
        );

        // Set enrolled students without deep references
        contest.setEnrolledStudents(
                contestDTO.getEnrolledStudents().stream()
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
        return new NewContestDTO(
                contest.getId(),
                contest.getTitle(),
                contest.getDescription(),
                contest.getStartTime(),
                contest.getEndTime(),
                contest.getTotalMarks(),
                contest.getDifficultyLevel(),
                contest.getCreatedBy().getId(),
                contest.getContestQuestions().stream()
                        .map(question -> new ContestQuestionDTO(question.getQuestion().getId(), question.getMarks()))
                        .collect(Collectors.toList()),
                contest.getEnrolledStudents().stream()
                        .map(student -> new StudentDTO(student.getId(), student.getName(), student.getEmail()))
                        .collect(Collectors.toList())
        );
    }
}
