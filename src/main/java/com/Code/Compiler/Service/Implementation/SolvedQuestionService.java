package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.Mapper.ContestResultMapper;
import com.Code.Compiler.Repository.SolvedQuestionInContestRepository;
import com.Code.Compiler.models.ContestQuestion;
import com.Code.Compiler.models.SolvedQuestionInContest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolvedQuestionService {

    @Autowired
    private SolvedQuestionInContestRepository solvedQuestionInContestRepository;

    // Save or Update a solved question
    public void saveOrUpdateSolvedQuestion(SolvedQuestionInContestDTO solvedQuestionDTO) {
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
            existingSolvedQuestion.setContestQuestion(new ContestQuestion(solvedQuestionDTO.getContestQuestionId()));
            solvedQuestionInContestRepository.save(existingSolvedQuestion);
        } else {
            // If no existing record, save a new one
            SolvedQuestionInContest newSolvedQuestion = ContestResultMapper.toSolvedQuestionInContestEntity(solvedQuestionDTO);
            solvedQuestionInContestRepository.save(newSolvedQuestion);
        }
    }

    // Get all solved questions by contest ID
    public List<SolvedQuestionInContestDTO> getAllSolvedQuestionsByContestId(Long contestId) {
        // Fetch all solved questions for the given contest ID
        List<SolvedQuestionInContest> solvedQuestions = solvedQuestionInContestRepository.findByContestId(contestId);

        // Convert to DTOs
        return solvedQuestions.stream()
                .map(ContestResultMapper::toSolvedQuestionInContestDTO)
                .collect(Collectors.toList());
    }

    // Get all solved questions by student ID and contest ID
    public List<SolvedQuestionInContestDTO> getAllSolvedQuestionsByStudentIdAndContestId(Long studentId, Long contestId) {
        // Fetch all solved questions for the given student and contest ID
        List<SolvedQuestionInContest> solvedQuestions = solvedQuestionInContestRepository
                .findByStudentIdAndContestId(studentId, contestId);

        // Convert to DTOs
        return solvedQuestions.stream()
                .map(ContestResultMapper::toSolvedQuestionInContestDTO)
                .collect(Collectors.toList());
    }

    // Delete a solved question by ID
    public void deleteSolvedQuestionById(Long solvedQuestionId) {
        SolvedQuestionInContest solvedQuestion = solvedQuestionInContestRepository.findById(solvedQuestionId)
                .orElseThrow(() -> new RuntimeException("Solved question not found with ID: " + solvedQuestionId));
        solvedQuestionInContestRepository.delete(solvedQuestion);
    }
}
