package com.Code.Compiler.Controllers;



import com.Code.Compiler.Service.Implementation.ExamplesServiceImpl;
import com.Code.Compiler.Exceptions.ExampleNotFoundException;
import com.Code.Compiler.models.Examples;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/examples")
public class ExampleController {

    @Autowired
    private ExamplesServiceImpl examplesService;

    @GetMapping
    public List<Examples> getAllExamples() {
        return examplesService.getAllExamples();
    }

    @GetMapping("/{id}")
    public Examples getExampleById(@PathVariable Long id) {
        return examplesService.getExampleById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Examples createExample(@RequestBody Examples example) {
        return examplesService.createExample(example);
    }

    @PutMapping("/{id}")
    public Examples updateExample(@PathVariable Long id, @RequestBody Examples exampleDetails) {
        return examplesService.updateExample(id, exampleDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExample(@PathVariable Long id) {
        examplesService.deleteExample(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleExampleNotFound(ExampleNotFoundException exception) {
        return exception.getMessage();
    }
}
