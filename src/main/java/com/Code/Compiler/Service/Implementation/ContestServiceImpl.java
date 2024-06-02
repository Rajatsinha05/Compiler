package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.Exceptions.ContestNotFoundException;
import com.Code.Compiler.Exceptions.UserNotFoundException;
import com.Code.Compiler.Repository.ContestRepository;
import com.Code.Compiler.Repository.UserRepository;

import com.Code.Compiler.Service.Interfaces.IContestService;
import com.Code.Compiler.models.Contest;
import com.Code.Compiler.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContestServiceImpl implements IContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    @Override
    public Optional<Contest> getContestById(Long id) {
        return contestRepository.findById(id);
    }

    @Override
    public Contest createContest(Contest contest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        contest.setCreatedBy(user);
        return contestRepository.save(contest);
    }

    @Override
    public Contest updateContestDetails(Long id, Contest contestDetails) {
        Optional<Contest> contest = contestRepository.findById(id);
        if (contest.isPresent()) {
            Contest existingContest = contest.get();
            existingContest.setTitle(contestDetails.getTitle());
            existingContest.setDescription(contestDetails.getDescription());
            existingContest.setStartTime(contestDetails.getStartTime());
            existingContest.setEndTime(contestDetails.getEndTime());
            existingContest.setTotalMarks(contestDetails.getTotalMarks());
            existingContest.setDifficultyLevel(contestDetails.getDifficultyLevel());
            existingContest.setQuestions(contestDetails.getQuestions());
            existingContest.setEnrolledStudents(contestDetails.getEnrolledStudents());
            return contestRepository.save(existingContest);
        } else {
            throw new ContestNotFoundException("Contest not found with id: " + id);
        }
    }

    @Override
    public void deleteContest(Long id) {
        contestRepository.deleteById(id);
    }
}
