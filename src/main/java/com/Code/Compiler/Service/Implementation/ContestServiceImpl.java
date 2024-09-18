package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.DTO.ContestQuestionDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.Enum.Role;
import com.Code.Compiler.Exceptions.ContestNotFoundException;
import com.Code.Compiler.Exceptions.UserNotFoundException;
import com.Code.Compiler.Mapper.ContestMapper;
import com.Code.Compiler.Repository.ContestRepository;
import com.Code.Compiler.Repository.ContestQuestionRepository; // Add ContestQuestionRepository
import com.Code.Compiler.Repository.QuestionsRepository;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.Service.Interfaces.IContestService;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.ContestQuestion; // Import ContestQuestion
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContestServiceImpl implements IContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestQuestionRepository contestQuestionRepository; // Inject ContestQuestionRepository

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private StudentRepository studentRepository;

    // login students
    public List<ContestDTO> getAllContestsForLoggedInStudent() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            User user = userRepository.findByEmail(username);
            if (user == null) {
                throw new UserNotFoundException("User not found with email: " + username);
            }

            if (user.getRole() == Role.STUDENT) {
                Students student = studentRepository.findById(user.getId())
                        .orElseThrow(() -> new UserNotFoundException("Student not found with user id: " + user.getId()));
                List<Contest> enrolledContests = contestRepository.findAll().stream()
                        .filter(contest -> contest.getEnrolledStudents().contains(student))
                        .collect(Collectors.toList());
                return enrolledContests.stream()
                        .map(ContestMapper::toDTO)
                        .collect(Collectors.toList());
            } else {
                throw new SecurityException("User is not a student");
            }
        } else {
            throw new SecurityException("Principal is not of type User");
        }
    }

    @Override
    public List<ContestDTO> getAllContests() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User loggedInUser = (User) principal;
            User user = userRepository.findByEmail(loggedInUser.getEmail());
            if (user == null) {
                throw new UserNotFoundException("User not found with email: " + loggedInUser.getUsername());
            }

            if (user.getRole() == Role.STUDENT) {
                Students student = studentRepository.findById(user.getId())
                        .orElseThrow(() -> new UserNotFoundException("Student not found with user id: " + user.getId()));
                return contestRepository.findAll().stream()
                        .filter(contest -> contest.getEnrolledStudents().contains(student))
                        .map(ContestMapper::toDTO)
                        .collect(Collectors.toList());
            } else {
                return contestRepository.findAll().stream()
                        .map(ContestMapper::toDTO)
                        .collect(Collectors.toList());
            }

        } else if (principal instanceof Students) {
            Students student = (Students) principal;
            return contestRepository.findAll().stream()
                    .filter(contest -> contest.getEnrolledStudents().contains(student))
                    .map(ContestMapper::toDTO)
                    .collect(Collectors.toList());

        } else {
            throw new SecurityException("Principal is not of type User or Student");
        }
    }

    @Override
    public Optional<ContestDTO> getContestById(Long id) {
        Optional<Contest> contest = contestRepository.findById(id);
        return contest.map(ContestMapper::toDTO);
    }


    @Override
    public ContestDTO createContest(ContestDTO contestDTO) {
        // Find the user by ID
        User user = userRepository.findById(contestDTO.getCreatedById())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + contestDTO.getCreatedById()));

        // Create Contest entity from DTO
        Contest contest = ContestMapper.toEntity(contestDTO, user, null, null);

        // Save the contest first to generate an ID
        Contest savedContest = contestRepository.save(contest);

        // Initialize contestQuestions to an empty list
        List<ContestQuestion> contestQuestions = Collections.emptyList();

        // Check if contestDTO.getContestQuestions() is not null before streaming
        if (contestDTO.getContestQuestions() != null) {
            contestQuestions = contestDTO.getContestQuestions().stream()
                    .map(contestQuestionDTO -> {
                        Questions question = questionsRepository.findById(contestQuestionDTO.getQuestionId())
                                .orElseThrow(() -> new RuntimeException("Question not found with id: " + contestQuestionDTO.getQuestionId()));
                        // Create a new ContestQuestion linking the contest and the question
                        return new ContestQuestion(null, savedContest, question, contestQuestionDTO.getMarks());
                    })
                    .collect(Collectors.toList());
        }

        // Save all contest questions to the repository
        contestQuestionRepository.saveAll(contestQuestions);

        // Associate saved questions with the contest
        savedContest.setContestQuestions(contestQuestions);

        // Return the complete DTO
        return ContestMapper.toDTO(savedContest);
    }


    @Override
    public ContestDTO updateContestDetails(Long id, ContestDTO contestDTO) {
        Contest existingContest = contestRepository.findById(id)
                .orElseThrow(() -> new ContestNotFoundException("Contest not found with id: " + id));
        User user = userRepository.findById(contestDTO.getCreatedById())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + contestDTO.getCreatedById()));

        existingContest.setTitle(contestDTO.getTitle());
        existingContest.setDescription(contestDTO.getDescription());
        existingContest.setStartTime(contestDTO.getStartTime());
        existingContest.setEndTime(contestDTO.getEndTime());
        existingContest.setTotalMarks(contestDTO.getTotalMarks());
        existingContest.setDifficultyLevel(contestDTO.getDifficultyLevel());
        existingContest.setCreatedBy(user);

        Contest updatedContest = contestRepository.save(existingContest);

        // Update ContestQuestion entities
        contestQuestionRepository.deleteByContestId(updatedContest.getId());
        List<ContestQuestion> updatedContestQuestions = contestDTO.getContestQuestions().stream()
                .map(contestQuestionDTO -> {
                    Questions question = questionsRepository.findById(contestQuestionDTO.getQuestionId())
                            .orElseThrow(() -> new RuntimeException("Question not found with id: " + contestQuestionDTO.getQuestionId()));
                    return new ContestQuestion(null, updatedContest, question, contestQuestionDTO.getMarks());
                })
                .collect(Collectors.toList());
        contestQuestionRepository.saveAll(updatedContestQuestions);

        return ContestMapper.toDTO(updatedContest);
    }

    @Override
    public void deleteContest(Long id) {
        if (!contestRepository.existsById(id)) {
            throw new ContestNotFoundException("Contest not found with id: " + id);
        }
        contestRepository.deleteById(id);
    }
}
