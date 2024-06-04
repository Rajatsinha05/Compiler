package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.QuestionsDTO;
import com.Code.Compiler.models.Questions;

public class QuestionsMapper {
    public static QuestionsDTO toDTO(Questions question) {
        QuestionsDTO dto = new QuestionsDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setDescription(question.getDescription());
        dto.setDifficultLevel(question.getDifficultLevel());

        // Mapping createdBy field
        dto.setUser(UserMapper.toDTO(question.getUser()));

        return dto;
    }
}
