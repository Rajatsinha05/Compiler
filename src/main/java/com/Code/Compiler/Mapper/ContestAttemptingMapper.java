package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.ContestAttemptingDTO;
import com.Code.Compiler.models.ContestAttempting;

public class ContestAttemptingMapper {
    public static ContestAttemptingDTO toDTO(ContestAttempting contestAttempting) {
        ContestAttemptingDTO dto = new ContestAttemptingDTO();

        // Set basic information
        dto.setId(contestAttempting.getId());
        dto.setContestId(contestAttempting.getContest().getId());
        dto.setStudentId(contestAttempting.getStudent().getId());
        dto.setTotalMarks(contestAttempting.getContest().getTotalMarks());
        dto.setStartTime(contestAttempting.getStartTime());
        dto.setEndTime(contestAttempting.getEndTime());

        // Set student's information
        dto.setUsername(contestAttempting.getStudent().getName());
        dto.setEmail(contestAttempting.getStudent().getEmail());

        // Set contest's total marks (assuming Contest object has a method getTotalMarks)
        dto.setTotalMarks(contestAttempting.getContest().getTotalMarks());

        return dto;
    }
}
