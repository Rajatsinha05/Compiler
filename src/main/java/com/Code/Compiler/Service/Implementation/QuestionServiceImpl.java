package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.QuestionsDTO;
import com.Code.Compiler.Enum.DifficultLevel;
import com.Code.Compiler.Exceptions.QuestionNotFoundException;
import com.Code.Compiler.Mapper.ExamplesMapper;
import com.Code.Compiler.Mapper.QuestionsMapper;
import com.Code.Compiler.Repository.QuestionsRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.models.Examples;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private QuestionsRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<QuestionsDTO> getAllQuestions() {
        logger.info("Fetching all questions");
        List<Questions> questions = questionRepository.findAll();
        List<QuestionsDTO> questionDTOs = questions.stream()
                .map(QuestionsMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Fetched {} questions", questionDTOs.size());
        return questionDTOs;
    }

    public Optional<QuestionsDTO> getQuestionById(Long id) {
        logger.info("Fetching question with id: {}", id);
        Optional<Questions> question = questionRepository.findById(id);
        Optional<QuestionsDTO> questionDTO = question.map(QuestionsMapper::toDTO);
        if (questionDTO.isPresent()) {
            logger.info("Question found with id: {}", id);
        } else {
            logger.warn("Question not found with id: {}", id);
        }
        return questionDTO;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public QuestionsDTO createQuestion(QuestionsDTO questionDTO) {
        logger.info("Creating new question: {}", questionDTO.getTitle());
        // Ensure the user is set correctly
        User user = null;
        if (questionDTO.getUserId() != null) {
            Optional<User> optionalUser = userRepository.findById(questionDTO.getUserId());
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            } else {
                logger.warn("User not found with id: {}", questionDTO.getUserId());
                throw new QuestionNotFoundException("User not found with id: " + questionDTO.getUserId());
            }
        }
        Questions question = QuestionsMapper.toEntity(questionDTO, user, userRepository);

//        question.setId(ThreadLocalRandom.current().nextLong(1_000_000_000_000L, Long.MAX_VALUE));
        Questions savedQuestion = questionRepository.save(question);
        logger.info("Question created with id: {}", savedQuestion.getId());
        return QuestionsMapper.toDTO(savedQuestion);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public void deleteQuestion(Long id) {
        logger.info("Deleting question with id: {}", id);
        questionRepository.deleteById(id);
        logger.info("Question deleted with id: {}", id);
    }

    public QuestionsDTO updateQuestionDetails(Long id, QuestionsDTO questionDTO) {
        logger.info("Updating question with id: {}", id);
        Optional<Questions> question = questionRepository.findById(id);
        if (question.isPresent()) {
            Questions existingQuestion = question.get();
            existingQuestion.setTitle(questionDTO.getTitle());
            existingQuestion.setDescription(questionDTO.getDescription());
            existingQuestion.setDifficultLevel(DifficultLevel.valueOf(questionDTO.getDifficultLevel()));
            existingQuestion.setConstraintValue(questionDTO.getConstraintValue());
            existingQuestion.setInput(questionDTO.getInput());
            existingQuestion.setExpectedOutput(questionDTO.getExpectedOutput());
            // Ensure the user is set correctly
            User user = null;
            if (questionDTO.getUserId() != null) {
                Optional<User> optionalUser = userRepository.findById(questionDTO.getUserId());
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                } else {
                    logger.warn("User not found with id: {}", questionDTO.getUserId());
                    throw new QuestionNotFoundException("User not found with id: " + questionDTO.getUserId());
                }
            }
            existingQuestion.setUser(user);
            // Update examples and ensure user is set for each example
            if (questionDTO.getExamples() != null) {
                List<Examples> examples = questionDTO.getExamples().stream()
                        .map(examplesDTO -> ExamplesMapper.toEntity(examplesDTO, userRepository))
                        .collect(Collectors.toList());
                existingQuestion.setExamples(examples);
            }
            Questions updatedQuestion = questionRepository.save(existingQuestion);
            logger.info("Question updated with id: {}", updatedQuestion.getId());
            return QuestionsMapper.toDTO(updatedQuestion);
        } else {
            logger.warn("Question not found with id: {}", id);
            throw new QuestionNotFoundException("Question not found with id: " + id);
        }
    }
}
