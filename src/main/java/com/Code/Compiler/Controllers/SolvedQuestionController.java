package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.Service.Implementation.SolvedQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solved-questions")
public class SolvedQuestionController {

    @Autowired
    private SolvedQuestionService solvedQuestionService;

    // Endpoint to save or update a solved question
    @PostMapping("/save")
    public ResponseEntity<String> saveOrUpdateSolvedQuestion(@RequestBody SolvedQuestionInContestDTO solvedQuestionDTO) {
        try {
            solvedQuestionService.saveOrUpdateSolvedQuestion(solvedQuestionDTO);
            return ResponseEntity.ok("Solved question saved or updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to save or update solved question: " + e.getMessage());
        }
    }

    // Endpoint to get all solved questions by contest ID
    @GetMapping("/contest/{contestId}")
    public ResponseEntity<List<SolvedQuestionInContestDTO>> getAllSolvedQuestionsByContestId(@PathVariable Long contestId) {
        try {
            List<SolvedQuestionInContestDTO> solvedQuestionsDTO = solvedQuestionService.getAllSolvedQuestionsByContestId(contestId);
            return ResponseEntity.ok(solvedQuestionsDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint to get all solved questions by student ID and contest ID
    @GetMapping("/student/{studentId}/contest/{contestId}")
    public ResponseEntity<List<SolvedQuestionInContestDTO>> getAllSolvedQuestionsByStudentIdAndContestId(
            @PathVariable Long studentId, @PathVariable Long contestId) {
        try {
            List<SolvedQuestionInContestDTO> solvedQuestionsDTO = solvedQuestionService.getAllSolvedQuestionsByStudentIdAndContestId(studentId, contestId);
            return ResponseEntity.ok(solvedQuestionsDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint to delete a solved question by ID
    @DeleteMapping("/{solvedQuestionId}")
    public ResponseEntity<String> deleteSolvedQuestionById(@PathVariable Long solvedQuestionId) {
        try {
            solvedQuestionService.deleteSolvedQuestionById(solvedQuestionId);
            return ResponseEntity.ok("Solved question deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete solved question: " + e.getMessage());
        }
    }
}
