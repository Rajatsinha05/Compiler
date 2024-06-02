package com.Code.Compiler.Service.Interfaces;



import com.Code.Compiler.models.Contest;
import java.util.List;
import java.util.Optional;

public interface IContestService {
    List<Contest> getAllContests();
    Optional<Contest> getContestById(Long id);
    Contest createContest(Contest contest, Long userId);
    Contest updateContestDetails(Long id, Contest contestDetails);
    void deleteContest(Long id);
}
