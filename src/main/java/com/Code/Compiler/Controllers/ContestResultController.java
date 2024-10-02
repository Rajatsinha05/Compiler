package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.ContestResultDTO;
import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.Service.Implementation.ContestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contest-results")
public class ContestResultController {
    @Autowired
    private ContestResultService contestResultService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitContestResults(@RequestBody ContestResultDTO contestResultDTO) {
        try {
            contestResultService.saveStudentResult(contestResultDTO);
            return ResponseEntity.ok("Contest results submitted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to submit contest results: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContestResultDTO>> getAllContestResults() {
        List<ContestResultDTO> contestResults = contestResultService.getAllContestResults();
        return ResponseEntity.ok(contestResults);
    }

    @GetMapping("/{contestId}/student/{studentId}")
    public ResponseEntity<ContestResultDTO> getContestResult(
            @PathVariable Long contestId,
            @PathVariable Long studentId) {
        try {
            ContestResultDTO contestResult = contestResultService.getContestResultByContestAndStudent(contestId, studentId);
            return ResponseEntity.ok(contestResult);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateContestResult(
            @RequestParam Long contestResultId,
            @RequestBody List<SolvedQuestionInContestDTO> solvedQuestionsDTOs) {
        try {
            contestResultService.updateContestResult(contestResultId, solvedQuestionsDTOs);
            return ResponseEntity.ok("Contest result updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update contest result: " + e.getMessage());
        }
    }

    @DeleteMapping("/{contestId}/student/{studentId}/delete")
    public ResponseEntity<String> deleteContestResult(
            @PathVariable Long contestId,
            @PathVariable Long studentId) {
        try {
            contestResultService.deleteContestResult(contestId, studentId);
            return ResponseEntity.ok("Contest result deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete contest result: " + e.getMessage());
        }
    }



}
