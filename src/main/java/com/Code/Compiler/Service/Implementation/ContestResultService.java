package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.ContestResultDTO;
import com.Code.Compiler.DTO.SolvedQuestionInContestDTO;
import com.Code.Compiler.Mapper.ContestResultMapper;
import com.Code.Compiler.Repository.*;
import com.Code.Compiler.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ContestResultService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ContestResultRepository contestResultRepository;

    @Autowired
    private ContestQuestionRepository contestQuestionRepository;

    @Autowired
    private SolvedQuestionInContestRepository solvedQuestionInContestRepository;

    public void saveStudentResult(ContestResultDTO contestResultDTO) {
        Contest contest = getContestById(contestResultDTO.getContestId());
        Students student = getStudentById(contestResultDTO.getStudentId());

        ContestResult contestResult = ContestResultMapper.toEntity(contestResultDTO, contest, student);
//        contestResult.setId(ThreadLocalRandom.current().nextLong(1_000_000_000_000L, Long.MAX_VALUE));
        contestResultRepository.save(contestResult);
    }

    public List<ContestResultDTO> getAllContestResults() {
        List<ContestResult> contestResults = contestResultRepository.findAll();
        return contestResults.stream()
                .map(ContestResultMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ContestResultDTO getContestResultByContestAndStudent(Long contestId, Long studentId) {
        ContestResult contestResult = contestResultRepository.findByContestIdAndStudentId(contestId, studentId)
                .orElseThrow(() -> new RuntimeException("Contest result not found for student with ID: " + studentId + " in contest with ID: " + contestId));
        return ContestResultMapper.toDTO(contestResult);
    }

    public void updateContestResult(Long contestResultId, List<SolvedQuestionInContestDTO> solvedQuestionsDTOs) {
        ContestResult contestResult = contestResultRepository.findById(contestResultId)
                .orElseThrow(() -> new RuntimeException("Contest result not found with ID: " + contestResultId));

        List<SolvedQuestionInContest> solvedQuestions = solvedQuestionsDTOs.stream()
                .map(ContestResultMapper::toSolvedQuestionInContestEntity)
                .collect(Collectors.toList());

        solvedQuestions.forEach(solvedQuestion -> solvedQuestion.setContest(contestResult.getContest()));
        solvedQuestions.forEach(solvedQuestion -> solvedQuestion.setStudent(contestResult.getStudent()));

        contestResult.setSolvedQuestions(solvedQuestions);

        contestResultRepository.save(contestResult);
    }

    public void deleteContestResult(Long contestId, Long studentId) {
        ContestResult contestResult = contestResultRepository.findByContestIdAndStudentId(contestId, studentId)
                .orElseThrow(() -> new RuntimeException("Contest result not found for student with ID: " + studentId + " in contest with ID: " + contestId));

        contestResultRepository.delete(contestResult);
    }

    private Contest getContestById(Long contestId) {
        return contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found with ID: " + contestId));
    }

    private Students getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    public void saveSolvedQuestion(SolvedQuestionInContestDTO solvedQuestionDTO) {
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

    // Method to get all solved questions by contest ID and convert to DTOs
    public List<SolvedQuestionInContestDTO> getAllSolvedQuestionsByContestId(Long contestId) {
        // Fetch all solved questions for the given contest ID
        List<SolvedQuestionInContest> solvedQuestions = solvedQuestionInContestRepository.findByContestId(contestId);

        // Convert to DTOs
        return solvedQuestions.stream()
                .map(ContestResultMapper::toSolvedQuestionInContestDTO)
                .collect(Collectors.toList());
    }
    public List<SolvedQuestionInContestDTO> getSolvedQuestionInContest() {
        // Fetch all solved questions from the repository
        List<SolvedQuestionInContest> solvedQuestions = solvedQuestionInContestRepository.findAll();

        // Convert to DTOs before returning
        return solvedQuestions.stream()
                .map(ContestResultMapper::toSolvedQuestionInContestDTO)
                .collect(Collectors.toList());
    }


}
