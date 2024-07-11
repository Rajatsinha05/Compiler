package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.QuestionsDTO;
import com.Code.Compiler.Service.Implementation.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionServiceImpl questionService;

    @GetMapping
    public List<QuestionsDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Optional<QuestionsDTO> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping
    public QuestionsDTO createQuestion(@RequestBody QuestionsDTO questionDTO) {
        return questionService.createQuestion(questionDTO);
    }

    @PutMapping("/{id}")
    public QuestionsDTO updateQuestion(@PathVariable Long id, @RequestBody QuestionsDTO questionDTO) {
        return questionService.updateQuestionDetails(id, questionDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }
}
