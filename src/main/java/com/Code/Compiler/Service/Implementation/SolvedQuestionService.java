package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.Mapper.SolvedQuestionMapper;
import com.Code.Compiler.Repository.*;
import com.Code.Compiler.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolvedQuestionService {

    @Autowired
    private SolvedQuestionInContestRepository solvedQuestionInContestRepository;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private StudentRepository studentsRepository;

    @Autowired
    private ContestQuestionRepository contestQuestionRepository;

    // Save or Update a solved question
    @Transactional
    public void saveOrUpdateSolvedQuestion(SolvedQuestionInContestDTO solvedQuestionDTO) {
        if (solvedQuestionDTO == null) {
            throw new IllegalArgumentException("SolvedQuestionInContestDTO cannot be null");
        }

        SolvedQuestionInContest solvedQuestionEntity;

        // If the ID is present, try to update the existing entry
        if (solvedQuestionDTO.getId() != null) {
            solvedQuestionEntity = solvedQuestionInContestRepository.findById(solvedQuestionDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Solved question not found with ID: " + solvedQuestionDTO.getId()));

            // Update the obtained marks
            solvedQuestionEntity.setObtainedMarks(solvedQuestionDTO.getObtainedMarks());

            if (solvedQuestionDTO.getContestQuestionId() != null) {
                ContestQuestion contestQuestion = contestQuestionRepository.findById(solvedQuestionDTO.getContestQuestionId())
                        .orElseThrow(() -> new RuntimeException("Contest question not found with ID: " + solvedQuestionDTO.getContestQuestionId()));
                solvedQuestionEntity.setContestQuestion(contestQuestion);
            }

        } else {
            // If no ID is provided, check if an entry exists with the given contestId, studentId, and questionId
            Questions question = questionsRepository.findById(solvedQuestionDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found with ID: " + solvedQuestionDTO.getQuestionId()));
            Contest contest = contestRepository.findById(solvedQuestionDTO.getContestId())
                    .orElseThrow(() -> new RuntimeException("Contest not found with ID: " + solvedQuestionDTO.getContestId()));
            Students student = studentsRepository.findById(solvedQuestionDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + solvedQuestionDTO.getStudentId()));
            ContestQuestion contestQuestion = contestQuestionRepository.findById(solvedQuestionDTO.getContestQuestionId())
                    .orElse(null);

            solvedQuestionEntity = SolvedQuestionMapper.toSolvedQuestionInContestEntity(solvedQuestionDTO, question, contest, student, contestQuestion);
        }

        // Save the solved question entity
        solvedQuestionInContestRepository.save(solvedQuestionEntity);
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
                .map(SolvedQuestionMapper::toSolvedQuestionInContestDTO)
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
                .map(SolvedQuestionMapper::toSolvedQuestionInContestDTO)
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

    // Update obtained marks for a solved question
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
