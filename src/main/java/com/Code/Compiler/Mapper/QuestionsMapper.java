package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ExamplesDTO;
import com.Code.Compiler.DTO.QuestionsDTO;
import com.Code.Compiler.Enum.DifficultLevel;
import com.Code.Compiler.models.Examples;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.User;
import com.Code.Compiler.Repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionsMapper {

    // Convert Questions entity to QuestionsDTO
    public static QuestionsDTO toDTO(Questions question) {
        Long userId = question.getUser() != null ? question.getUser().getId() : null;
        List<ExamplesDTO> examples = question.getExamples() != null ?
                question.getExamples().stream().map(ExamplesMapper::toDTO).collect(Collectors.toList()) : null;

        return new QuestionsDTO(
                question.getId(),
                question.getTitle(),
                question.getDescription(),
                question.getTags(),
                question.getDifficultLevel() != null ? question.getDifficultLevel().name() : null,
                question.getConstraintValue(),
                question.getInput(),
                question.getExpectedOutput(),
                userId,
                examples
        );
    }

    // Convert QuestionsDTO to Questions entity
    public static Questions toEntity(QuestionsDTO questionDTO, User user, UserRepository userRepository) {
        Questions question = new Questions();
        question.setId(questionDTO.getId());
        question.setTitle(questionDTO.getTitle());
        question.setDescription(questionDTO.getDescription());
        question.setTags(questionDTO.getTags());

        // Handling DifficultLevel with null check
        question.setDifficultLevel(questionDTO.getDifficultLevel() != null ?
                DifficultLevel.valueOf(questionDTO.getDifficultLevel()) : null);

        question.setConstraintValue(questionDTO.getConstraintValue());
        question.setInput(questionDTO.getInput());
        question.setExpectedOutput(questionDTO.getExpectedOutput());

        // If user is passed, set it, otherwise look it up via repository
        if (user != null) {
            question.setUser(user);
        } else if (questionDTO.getUserId() != null) {
            question.setUser(userRepository.findById(questionDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + questionDTO.getUserId())));
        }

        // Handle examples conversion
        if (questionDTO.getExamples() != null) {
            List<Examples> examples = questionDTO.getExamples().stream()
                    .map(examplesDTO -> ExamplesMapper.toEntity(examplesDTO, userRepository))
                    .collect(Collectors.toList());
            question.setExamples(examples);
        }

        return question;
    }
}
