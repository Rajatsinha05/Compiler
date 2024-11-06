package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.ContestAttemptingDTO;
import com.Code.Compiler.Mapper.ContestAttemptingMapper;
import com.Code.Compiler.Repository.ContestAttemptingRepository;
import com.Code.Compiler.models.ContestAttempting;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.Repository.ContestRepository;
import com.Code.Compiler.Repository.StudentRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ContestAttemptingService {

    @Autowired
    private ContestAttemptingRepository contestAttemptingRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Start a contest attempt
    @Transactional
    public ContestAttemptingDTO startContestAttempt(Long contestId, Long studentId) {
        // Find the contest and student by ID
        Optional<Contest> contestOpt = contestRepository.findById(contestId);
        Optional<Students> studentOpt = studentRepository.findById(studentId);

        if (contestOpt.isEmpty() || studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Contest or Student not found");
        }

        Contest contest = contestOpt.get();
        Students student = studentOpt.get();

        // Check if the student has already started this contest and it is ongoing
        Optional<ContestAttempting> existingAttemptOpt = contestAttemptingRepository
                .findByContestAndStudentAndEndTimeIsNull(contest, student);

        if (existingAttemptOpt.isPresent()) {
            throw new IllegalArgumentException("Student already has an ongoing attempt for this contest.");
        }

        // Create a new contest attempt with startTime handled here
        ContestAttempting contestAttempting = new ContestAttempting();
        contestAttempting.setContest(contest);
        contestAttempting.setStudent(student);
        contestAttempting.setStartTime(LocalDateTime.now()); // Start time set here
//    contestAttempting.setId(ThreadLocalRandom.current().nextLong(1_000_000_000_000L, Long.MAX_VALUE));
        // Save the contest attempt to the repository
        ContestAttempting savedAttempt = contestAttemptingRepository.save(contestAttempting);
        return ContestAttemptingMapper.toDTO(savedAttempt);
    }

    // End a contest attempt
    @Transactional
    public ContestAttemptingDTO endContestAttempt(Long attemptId, int totalMarks) {
        // Find the contest attempt by ID
        ContestAttempting contestAttempting = contestAttemptingRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found with ID: " + attemptId));

        // Set the end time and total marks
        contestAttempting.setEndTime(LocalDateTime.now());
        contestAttempting.setTotalMarks(totalMarks);

        // Save the updated contest attempt
        ContestAttempting savedAttempt = contestAttemptingRepository.save(contestAttempting);
        return ContestAttemptingMapper.toDTO(savedAttempt);
    }

    // Get a contest attempt by ID
    public Optional<ContestAttemptingDTO> getContestAttemptById(Long attemptId) {
        return contestAttemptingRepository.findById(attemptId)
                .map(ContestAttemptingMapper::toDTO);
    }

    // Get all attempts for a specific contest by contestId
    public List<ContestAttemptingDTO> getAttemptsByContestId(Long contestId) {
        // Find the contest by ID
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new IllegalArgumentException("Contest not found with ID: " + contestId));

        // Find all attempts for the contest and map to DTOs
        return contestAttemptingRepository.findByContest(contest)
                .stream()
                .map(ContestAttemptingMapper::toDTO)
                .collect(Collectors.toList());
    }
}
