package com.Code.Compiler.Controllers;

import com.Code.Compiler.Service.Interfaces.IContestResultService;
import com.Code.Compiler.models.ContestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contests/{contestId}/results")
public class ContestResultController {

    @Autowired
    private IContestResultService contestResultService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContestResult publishContestResults(@PathVariable Long contestId, @RequestBody List<ContestResult> results) {
        return contestResultService.publishContestResults(contestId, results);
    }

    @GetMapping
    public List<ContestResult> getAllResultsForContest(@PathVariable Long contestId) {
        return contestResultService.getAllResultsForContest(contestId);
    }

    @GetMapping("/{studentId}")
    public ContestResult getResultForStudent(@PathVariable Long contestId, @PathVariable Long studentId) {
        return contestResultService.getResultForStudent(contestId, studentId);
    }
}
