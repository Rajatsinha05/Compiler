package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.ContestAttemptingDTO;
import com.Code.Compiler.Service.Implementation.ContestAttemptingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/contest-attempts")
public class ContestAttemptingController {

    @Autowired
    private ContestAttemptingService contestAttemptingService;

    // Endpoint to start a contest attempt
    @PostMapping("/start")
    public ResponseEntity<?> startContestAttempt(@RequestBody Map<String, Object> requestData) {
        try {
            Long contestId = Long.valueOf(requestData.get("contestId").toString());
            Long studentId = Long.valueOf(requestData.get("studentId").toString());

            // Start contest attempt without passing startTime, handled in the service
            ContestAttemptingDTO contestAttempt = contestAttemptingService.startContestAttempt(contestId, studentId);
            return ResponseEntity.ok(contestAttempt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Failed to start contest attempt: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input data: " + e.getMessage());
        }
    }

    @PostMapping("/end")
    public ResponseEntity<?> endContestAttempt(@RequestParam Long attemptId, @RequestParam int totalMarks) {
        try {
            ContestAttemptingDTO contestAttempt = contestAttemptingService.endContestAttempt(attemptId, totalMarks);
            return ResponseEntity.ok(contestAttempt);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Failed to end contest attempt: " + e.getMessage());
        }
    }


    // Endpoint to get a contest attempt by ID
    @GetMapping("/{attemptId}")
    public ResponseEntity<?> getContestAttemptById(@PathVariable Long attemptId) {
        Optional<ContestAttemptingDTO> contestAttempt = contestAttemptingService.getContestAttemptById(attemptId);
        return contestAttempt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to get all contest attempts by contest ID
    @GetMapping("/by-contest/{contestId}")
    public ResponseEntity<?> getAttemptsByContestId(@PathVariable Long contestId) {
        try {
            List<ContestAttemptingDTO> contestAttempts = contestAttemptingService.getAttemptsByContestId(contestId);
            return ResponseEntity.ok(contestAttempts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Failed to get contest attempts: " + e.getMessage());
        }
    }
}
