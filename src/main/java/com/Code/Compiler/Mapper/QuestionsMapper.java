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

    public static QuestionsDTO toDTO(Questions question) {
        Long userId = question.getUser() != null ? question.getUser().getId() : null;
        List<ExamplesDTO> examples = question.getExamples() != null ? question.getExamples().stream().map(ExamplesMapper::toDTO).collect(Collectors.toList()) : null;
        return new QuestionsDTO(
                question.getId(),
                question.getTitle(),
                question.getDescription(),
                question.getTags(),
                question.getDifficultLevel().name(),
                question.getConstraintValue(),
                question.getInput(),
                question.getExpectedOutput(),
                userId,
                examples
        );
    }

    public static Questions toEntity(QuestionsDTO questionDTO, User user, UserRepository userRepository) {
        Questions question = new Questions();
        question.setId(questionDTO.getId());
        question.setTitle(questionDTO.getTitle());
        question.setDescription(questionDTO.getDescription());
        question.setTags(questionDTO.getTags());
        question.setDifficultLevel(DifficultLevel.valueOf(questionDTO.getDifficultLevel()));
        question.setConstraintValue(questionDTO.getConstraintValue());
        question.setInput(questionDTO.getInput());
        question.setExpectedOutput(questionDTO.getExpectedOutput());
        question.setUser(user);
        if (questionDTO.getExamples() != null) {
            List<Examples> examples = questionDTO.getExamples().stream()
                    .map(examplesDTO -> ExamplesMapper.toEntity(examplesDTO, userRepository))
                    .collect(Collectors.toList());
            question.setExamples(examples);
        }
        return question;
    }
}
