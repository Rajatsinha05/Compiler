package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.ContestDTO;
import com.Code.Compiler.DTO.QuestionDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.Enum.Role;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContestServiceImpl implements IContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private StudentRepository studentRepository;


//    login students

    // Improved method to fetch all contests for the logged-in student
    public List<ContestDTO> getAllContestsForLoggedInStudent() {
        // Retrieve the currently authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();

            // Find the user by email (username)
            User user = userRepository.findByEmail(username);
            if (user == null) {
                throw new UserNotFoundException("User not found with email: " + username);
            }

            // Check if the user is a student
            if (user.getRole() == Role.STUDENT) {
                // Find the associated student entity
                Students student = studentRepository.findById(user.getId())
                        .orElseThrow(() -> new UserNotFoundException("Student not found with user id: " + user.getId()));

                // Retrieve contests where the student is enrolled
                List<Contest> enrolledContests = contestRepository.findAll().stream()
                        .filter(contest -> contest.getEnrolledStudents().contains(student))
                        .collect(Collectors.toList());

                // Map the contests to DTOs
                return enrolledContests.stream()
                        .map(contest -> {
                            List<QuestionDTO> questionDTOs = contest.getQuestions().stream()
                                    .map(question -> new QuestionDTO(question.getId(), question.getTitle()))
                                    .collect(Collectors.toList());

                            List<StudentDTO> studentDTOs = contest.getEnrolledStudents().stream()
                                    .map(enrolledStudent -> new StudentDTO(enrolledStudent.getId(), enrolledStudent.getName(), enrolledStudent.getEmail()))
                                    .collect(Collectors.toList());

                            ContestDTO contestDTO = ContestMapper.toDTO(contest);
                            contestDTO.setQuestions(questionDTOs);  // Add question details
                            contestDTO.setEnrolledStudents(studentDTOs);  // Add enrolled student details
                            return contestDTO;
                        })
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
        // Retrieve the principal of the currently logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User loggedInUser = (User) principal;
            // Find the user by email
            User user = userRepository.findByEmail(loggedInUser.getEmail());
            if (user == null) {
                throw new UserNotFoundException("User not found with email: " + loggedInUser.getUsername());
            }

            // Check user role and retrieve contests accordingly
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
        } else {
            throw new SecurityException("Principal is not of type User");
        }
    }



    @Override
    public Optional<ContestDTO> getContestById(Long id) {
        return contestRepository.findById(id).map(ContestMapper::toDTO);
    }

    @Override
    public ContestDTO createContest(ContestDTO contestDTO) {
        User user = userRepository.findById(contestDTO.getCreatedById())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + contestDTO.getCreatedById()));

        List<Questions> questions = questionsRepository.findAllById(contestDTO.getQuestionIds());
        List<Students> students = studentRepository.findAllById(contestDTO.getEnrolledStudentIds());

        Contest contest = ContestMapper.toEntity(contestDTO, user, questions, students);
        Contest savedContest = contestRepository.save(contest);

        return ContestMapper.toDTO(savedContest);
    }

    @Override
    public ContestDTO updateContestDetails(Long id, ContestDTO contestDTO) {
        Contest existingContest = contestRepository.findById(id)
                .orElseThrow(() -> new ContestNotFoundException("Contest not found with id: " + id));
        User user = userRepository.findById(contestDTO.getCreatedById())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + contestDTO.getCreatedById()));

        List<Questions> questions = questionsRepository.findAllById(contestDTO.getQuestionIds());
        List<Students> students = studentRepository.findAllById(contestDTO.getEnrolledStudentIds());

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

    @Override
    public void deleteContest(Long id) {
        if (!contestRepository.existsById(id)) {
            throw new ContestNotFoundException("Contest not found with id: " + id);
        }
        contestRepository.deleteById(id);
    }
}
