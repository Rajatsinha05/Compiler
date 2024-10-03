package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.Mapper.ContestResultMapper;
import com.Code.Compiler.Repository.SolvedQuestionInContestRepository;
import com.Code.Compiler.models.ContestQuestion;
import com.Code.Compiler.models.SolvedQuestionInContest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolvedQuestionService {

    @Autowired
    private SolvedQuestionInContestRepository solvedQuestionInContestRepository;

    // Save or Update a solved question
    @Transactional
    public void saveOrUpdateSolvedQuestion(SolvedQuestionInContestDTO solvedQuestionDTO) {
        if (solvedQuestionDTO == null) {
            throw new IllegalArgumentException("SolvedQuestionInContestDTO cannot be null");
        }

        // Check if the solved question already exists by contestId, studentId, and questionId
        SolvedQuestionInContest existingSolvedQuestion = solvedQuestionInContestRepository
                .findByContestIdAndStudentIdAndQuestionId(
                        solvedQuestionDTO.getContestId(),
                        solvedQuestionDTO.getStudentId(),
                        solvedQuestionDTO.getQuestionId()
                );

        if (existingSolvedQuestion != null) {
            // Update the existing solved question
            existingSolvedQuestion.setObtainedMarks(solvedQuestionDTO.getObtainedMarks());

            // Check if the contest question is provided in DTO
            if (solvedQuestionDTO.getContestQuestionId() != null) {
                existingSolvedQuestion.setContestQuestion(new ContestQuestion(solvedQuestionDTO.getContestQuestionId()));
            }
            solvedQuestionInContestRepository.save(existingSolvedQuestion);
        } else {
            // If no existing record, save a new one
            SolvedQuestionInContest newSolvedQuestion = ContestResultMapper.toSolvedQuestionInContestEntity(solvedQuestionDTO);
            solvedQuestionInContestRepository.save(newSolvedQuestion);
        }
    }

    // Get all solved questions by contest ID
    public List<SolvedQuestionInContestDTO> getAllSolvedQuestionsByContestId(Long contestId) {
        if (contestId == null) {
            throw new IllegalArgumentException("Contest ID cannot be null");
        }

        // Fetch all solved questions for the given contest ID
        List<SolvedQuestionInContest> solvedQuestions = solvedQuestionInContestRepository.findByContestId(contestId);

        // Convert to DTOs
        return solvedQuestions.stream()
                .map(ContestResultMapper::toSolvedQuestionInContestDTO)
                .collect(Collectors.toList());
    }

    // Get all solved questions by student ID and contest ID
    public List<SolvedQuestionInContestDTO> getAllSolvedQuestionsByStudentIdAndContestId(Long studentId, Long contestId) {
        if (studentId == null || contestId == null) {
            throw new IllegalArgumentException("Student ID and Contest ID cannot be null");
        }

        // Fetch all solved questions for the given student and contest ID
        List<SolvedQuestionInContest> solvedQuestions = solvedQuestionInContestRepository
                .findByStudentIdAndContestId(studentId, contestId);

        // Convert to DTOs
        return solvedQuestions.stream()
                .map(ContestResultMapper::toSolvedQuestionInContestDTO)
                .collect(Collectors.toList());
    }

    // Delete a solved question by ID
    @Transactional
    public void deleteSolvedQuestionById(Long solvedQuestionId) {
        if (solvedQuestionId == null) {
            throw new IllegalArgumentException("Solved question ID cannot be null");
        }

        SolvedQuestionInContest solvedQuestion = solvedQuestionInContestRepository.findById(solvedQuestionId)
                .orElseThrow(() -> new RuntimeException("Solved question not found with ID: " + solvedQuestionId));
        solvedQuestionInContestRepository.delete(solvedQuestion);
    }
    @Transactional
    public void updateObtainedMarks(SolvedQuestionInContestDTO solvedQuestionDTO) {
        if (solvedQuestionDTO == null) {
            throw new IllegalArgumentException("SolvedQuestionInContestDTO cannot be null");
        }

        // Check if the solved question already exists by contestId, studentId, and questionId
        SolvedQuestionInContest existingSolvedQuestion = solvedQuestionInContestRepository
                .findByContestIdAndStudentIdAndQuestionId(
                        solvedQuestionDTO.getContestId(),
                        solvedQuestionDTO.getStudentId(),
                        solvedQuestionDTO.getQuestionId()
                );

        if (existingSolvedQuestion != null) {
            // Update the obtained marks
            existingSolvedQuestion.setObtainedMarks(solvedQuestionDTO.getObtainedMarks());
            solvedQuestionInContestRepository.save(existingSolvedQuestion);
        } else {
            throw new RuntimeException("Solved question not found for the given contestId, studentId, and questionId.");
        }
    }
}
