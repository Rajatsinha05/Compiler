package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.Exceptions.ContestNotFoundException;
import com.Code.Compiler.Exceptions.UserNotFoundException;
import com.Code.Compiler.Mapper.ContestMapper;
import com.Code.Compiler.Repository.ContestRepository;
import com.Code.Compiler.Repository.QuestionsRepository;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.Service.Interfaces.IContestService;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContestServiceImpl implements IContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Fetch all contests
    @Override
    public List<ContestDTO> getAllContests() {
        return contestRepository.findAll().stream()
                .map(ContestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get all contests by student ID
    @Override
    public List<ContestDTO> getAllContestsByStudentId(Long studentId) {
        Students student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException("Student not found with id: " + studentId));

        List<Contest> enrolledContests = contestRepository.findAll().stream()
                .filter(contest -> contest.getEnrolledStudents().contains(student))
                .collect(Collectors.toList());

        return enrolledContests.stream()
                .map(ContestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get contest by ID
    @Override
    public Optional<ContestDTO> getContestById(Long id) {
        return contestRepository.findById(id)
                .map(ContestMapper::toDTO);
    }

    // Create new contest
    @Override
    public ContestDTO createContest(ContestDTO contestDTO) {
        User user = userRepository.findById(contestDTO.getCreatedBy())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + contestDTO.getCreatedBy()));

        List<Questions> questions = questionsRepository.findAllById(
                contestDTO.getContestQuestions().stream()
                        .map(q -> q.getQuestionId())
                        .collect(Collectors.toList())
        );

        List<Students> students = studentRepository.findAllById(
                contestDTO.getEnrolledStudents().stream()
                        .map(StudentDTO::getId)
                        .collect(Collectors.toList())
        );

        Contest contest = ContestMapper.toEntity(contestDTO, user, questions, students);
        contest.setId(ThreadLocalRandom.current().nextLong(1_000_000_000_000L, Long.MAX_VALUE));
        Contest savedContest = contestRepository.save(contest);
        return ContestMapper.toDTO(savedContest);
    }

    // Update contest details
    @Override
    public ContestDTO updateContestDetails(Long id, ContestDTO contestDTO) {
        Contest existingContest = contestRepository.findById(id)
                .orElseThrow(() -> new ContestNotFoundException("Contest not found with id: " + id));

        User user = userRepository.findById(contestDTO.getCreatedBy())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + contestDTO.getCreatedBy()));

        List<Questions> questions = questionsRepository.findAllById(
                contestDTO.getContestQuestions().stream()
                        .map(q -> q.getQuestionId())
                        .collect(Collectors.toList())
        );

        List<Students> students = studentRepository.findAllById(
                contestDTO.getEnrolledStudents().stream()
                        .map(StudentDTO::getId)
                        .collect(Collectors.toList())
        );

        existingContest.setTitle(contestDTO.getTitle());
        existingContest.setDescription(contestDTO.getDescription());
        existingContest.setStartTime(contestDTO.getStartTime());
        existingContest.setEndTime(contestDTO.getEndTime());
        existingContest.setTotalMarks(contestDTO.getTotalMarks());
        existingContest.setDifficultyLevel(contestDTO.getDifficultyLevel());
        existingContest.setCreatedBy(user);
        existingContest.setQuestions(questions);
        existingContest.setEnrolledStudents(students);

        Contest updatedContest = contestRepository.save(existingContest);
        return ContestMapper.toDTO(updatedContest);
    }

    // Delete contest by ID
    @Override
    public void deleteContest(Long id) {
        if (!contestRepository.existsById(id)) {
            throw new ContestNotFoundException("Contest not found with id: " + id);
        }
        contestRepository.deleteById(id);
    }

    // Create a new contest (simple version without DTO)
    public Contest creatingContest(Contest contest) {
        return contestRepository.save(contest);
    }
}
