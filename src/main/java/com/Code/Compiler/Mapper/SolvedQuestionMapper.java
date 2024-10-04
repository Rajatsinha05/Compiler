package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.models.*;

public class SolvedQuestionMapper {

    // Map to entity for creation (ignoring the id)
    public static SolvedQuestionInContest toSolvedQuestionInContestEntity(SolvedQuestionInContestDTO dto,
                                                                          Questions question,
                                                                          Contest contest,
                                                                          Students student,
                                                                          ContestQuestion contestQuestion) {
        if (dto == null) {
            return null;
        }
        SolvedQuestionInContest entity = new SolvedQuestionInContest();
        // Set relationships using the provided entities
        entity.setQuestion(question);
        entity.setContest(contest);
        entity.setStudent(student);
        entity.setContestQuestion(contestQuestion);
        entity.setObtainedMarks(dto.getObtainedMarks());
        return entity;
    }

    // Map to DTO including the id (when fetching data)
    public static SolvedQuestionInContestDTO toSolvedQuestionInContestDTO(SolvedQuestionInContest entity) {
        if (entity == null) {
            return null;
        }
        return new SolvedQuestionInContestDTO(
                entity.getId(), // Include id when retrieving data
                entity.getQuestion() != null ? entity.getQuestion().getId() : null,
                entity.getContest() != null ? entity.getContest().getId() : null,
                entity.getStudent() != null ? entity.getStudent().getId() : null,
                entity.getContestQuestion() != null ? entity.getContestQuestion().getId() : null,
                entity.getObtainedMarks()
        );
    }
}
