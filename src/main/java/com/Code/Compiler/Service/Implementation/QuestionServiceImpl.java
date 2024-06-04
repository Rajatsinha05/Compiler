package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.QuestionsDTO;
import com.Code.Compiler.DTO.UserDTO;
import com.Code.Compiler.Exceptions.QuestionNotFoundException;
import com.Code.Compiler.Mapper.QuestionsMapper;
import com.Code.Compiler.Mapper.UserMapper;
import com.Code.Compiler.Repository.QuestionsRepository;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl {

    @Autowired
    private QuestionsRepository questionRepository;
@Transactional
    public List<QuestionsDTO> getAllQuestions() {

    List<Questions> questions = questionRepository.findAll();
    return questions.stream()
            .map(QuestionsMapper::toDTO)
            .collect(Collectors.toList());
    }

    public Optional<Questions> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public Questions createQuestion(Questions question) {
        return questionRepository.save(question);

    }
    public Questions updateQuestionDetails(Long id, Questions questionDetails) {
        Optional<Questions> question = questionRepository.findById(id);
        if (question.isPresent()) {
            Questions existingQuestion = question.get();
            existingQuestion.setTitle(questionDetails.getTitle());
            existingQuestion.setDescription(questionDetails.getDescription());
            existingQuestion.setDifficultLevel(questionDetails.getDifficultLevel());
            existingQuestion.setConstraintValue(questionDetails.getConstraintValue());
            existingQuestion.setInput(questionDetails.getInput());
            existingQuestion.setExpectedOutput(questionDetails.getExpectedOutput());
            return questionRepository.save(existingQuestion);
        } else {
            throw new QuestionNotFoundException("Question not found with id: " + id);
        }

    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
