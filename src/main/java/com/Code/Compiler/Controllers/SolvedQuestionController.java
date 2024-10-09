package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.DTO.StudentRankingDTO;
import com.Code.Compiler.Service.Implementation.SolvedQuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/solved-questions")
public class SolvedQuestionController {

    @Autowired
    private SolvedQuestionService solvedQuestionService;
@GetMapping("/top-20")
public List<StudentRankingDTO>  getSolvedQuestionsTop20() {
    return solvedQuestionService.getTop20RankedStudentsByTotalScore();
}
    // Endpoint to save or update a solved question
    @PostMapping("/save")
    public ResponseEntity<String> saveOrUpdateSolvedQuestion(@Valid @RequestBody SolvedQuestionInContestDTO solvedQuestionDTO) {
        try {
            solvedQuestionService.saveOrUpdateSolvedQuestion(solvedQuestionDTO);
            return ResponseEntity.ok("Solved question saved or updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save or update solved question: " + e.getMessage());
        }
    }

    // Endpoint to update obtained marks for a solved question
    @PutMapping("/update-marks")
    public ResponseEntity<String> updateObtainedMarks(@Valid @RequestBody SolvedQuestionInContestDTO solvedQuestionDTO) {
        try {
            solvedQuestionService.updateObtainedMarks(solvedQuestionDTO);
            return ResponseEntity.ok("Obtained marks updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solved question not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update obtained marks: " + e.getMessage());
        }
    }

    // Endpoint to get all solved questions by contest ID
    @GetMapping("/contest/{contestId}")
    public ResponseEntity<List<SolvedQuestionInContestDTO>> getAllSolvedQuestionsByContestId(@PathVariable Long contestId) {
        try {
            List<SolvedQuestionInContestDTO> solvedQuestionsDTO = solvedQuestionService.getAllSolvedQuestionsByContestId(contestId);
            if (solvedQuestionsDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(solvedQuestionsDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint to get all solved questions by student ID and contest ID
    @GetMapping("/student/{studentId}/contest/{contestId}")
    public ResponseEntity<List<SolvedQuestionInContestDTO>> getAllSolvedQuestionsByStudentIdAndContestId(
            @PathVariable Long studentId, @PathVariable Long contestId) {
        try {
            List<SolvedQuestionInContestDTO> solvedQuestionsDTO = solvedQuestionService.getAllSolvedQuestionsByStudentIdAndContestId(studentId, contestId);
            if (solvedQuestionsDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(solvedQuestionsDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint to delete a solved question by ID
    @DeleteMapping("/{solvedQuestionId}")
    public ResponseEntity<String> deleteSolvedQuestionById(@PathVariable Long solvedQuestionId) {
        try {
            solvedQuestionService.deleteSolvedQuestionById(solvedQuestionId);
            return ResponseEntity.ok("Solved question deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid solved question ID: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solved question not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete solved question: " + e.getMessage());
        }
    }
}
