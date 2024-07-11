package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.ContestDTO;
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

    @Override
    public List<ContestDTO> getAllContests() {
        return contestRepository.findAll().stream().map(ContestMapper::toDTO).collect(Collectors.toList());
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
        contestRepository.deleteById(id);
    }
}
