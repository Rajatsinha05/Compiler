package com.Code.Compiler.Service.Interfaces;

import com.Code.Compiler.DTO.ContestDTO;
import java.util.List;
import java.util.Optional;

public interface IContestService {
    List<ContestDTO> getAllContests();

    // Get all contests by student ID
    List<ContestDTO> getAllContestsByStudentId(Long studentId);

    Optional<ContestDTO> getContestById(Long id);
    ContestDTO createContest(ContestDTO contestDTO);
    ContestDTO updateContestDetails(Long id, ContestDTO contestDTO);
    void deleteContest(Long id);
}
