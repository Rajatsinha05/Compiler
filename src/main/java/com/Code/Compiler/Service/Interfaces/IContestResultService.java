package com.Code.Compiler.Service.Interfaces;

import com.Code.Compiler.models.ContestResult;

import java.util.List;

public interface IContestResultService {
    ContestResult publishContestResults(Long contestId, List<ContestResult> results);
    List<ContestResult> getAllResultsForContest(Long contestId);
    ContestResult getResultForStudent(Long contestId, Long studentId);
}
