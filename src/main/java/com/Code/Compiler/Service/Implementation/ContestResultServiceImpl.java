package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.Exceptions.ContestNotFoundException;
import com.Code.Compiler.Exceptions.StudentNotFoundException;
import com.Code.Compiler.Repository.ContestRepository;
import com.Code.Compiler.Repository.ContestResultRepository;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Service.Interfaces.IContestResultService;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.ContestResult;
import com.Code.Compiler.models.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ContestResultServiceImpl implements IContestResultService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ContestResultRepository contestResultRepository;

    @Override
    public ContestResult publishContestResults(Long contestId, List<ContestResult> results) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ContestNotFoundException("Contest not found with id: " + contestId));

        ContestResult contestResult = null;
        for (ContestResult result : results) {
            Students student = studentRepository.findById(result.getStudent().getId())
                    .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + result.getStudent().getId()));
            result.setContest(contest);
            result.setStudent(student);
//            result.setId(ThreadLocalRandom.current().nextLong(1_000_000_000_000L, Long.MAX_VALUE));
            contestResult = contestResultRepository.save(result);
        }
        return contestResult;
    }

    @Override
    public List<ContestResult> getAllResultsForContest(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ContestNotFoundException("Contest not found with id: " + contestId));
        return contest.getContestResults();
    }

    @Override
    public ContestResult getResultForStudent(Long contestId, Long studentId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ContestNotFoundException("Contest not found with id: " + contestId));
        return contest.getContestResults().stream()
                .filter(result -> result.getStudent().getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Result not found for student with id: " + studentId));
    }
}
