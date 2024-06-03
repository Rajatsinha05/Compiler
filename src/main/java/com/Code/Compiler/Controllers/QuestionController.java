package com.Code.Compiler.Controllers;

import com.Code.Compiler.Service.Implementation.QuestionServiceImpl;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.User;
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
    public List<Questions> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Optional<Questions> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping
    public Questions createQuestion(@RequestBody Questions question) {

        return questionService.createQuestion(question);
    }

    @PutMapping("/{id}")
    public Questions updateQuestion(@PathVariable Long id, @RequestBody Questions questionDetails) {
        return questionService.updateQuestionDetails(id, questionDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }
}
