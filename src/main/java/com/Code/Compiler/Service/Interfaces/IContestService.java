package com.Code.Compiler.Service.Interfaces;

import com.Code.Compiler.DTO.ContestDTO;
import java.util.List;
import java.util.Optional;

public interface IContestService {
    List<ContestDTO> getAllContests();
    Optional<ContestDTO> getContestById(Long id);
    ContestDTO createContest(ContestDTO contestDTO);
    ContestDTO updateContestDetails(Long id, ContestDTO contestDTO);
    List<ContestDTO> getAllContestsByStudentId(Long studentId);

    void deleteContest(Long id);
}
