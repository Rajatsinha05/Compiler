package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.Exceptions.ExampleNotFoundException;
import com.Code.Compiler.Repository.ExampleRepository;
import com.Code.Compiler.models.Examples;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamplesServiceImpl {

    @Autowired
    private ExampleRepository examplesRepository;

    public List<Examples> getAllExamples() {
        return examplesRepository.findAll();
    }

    public Examples getExampleById(Long id) {
        return examplesRepository.findById(id)
                .orElseThrow(() -> new ExampleNotFoundException("Example not found with id: " + id));
    }

    public Examples createExample(Examples example) {
        return examplesRepository.save(example);
    }

    public Examples updateExample(Long id, Examples exampleDetails) {
        Examples existingExample = getExampleById(id);
        existingExample.setInput(exampleDetails.getInput());
        existingExample.setOutput(exampleDetails.getOutput());
        existingExample.setExplanation(exampleDetails.getExplanation());
        return examplesRepository.save(existingExample);
    }

    public void deleteExample(Long id) {
        Examples example = getExampleById(id);
        examplesRepository.delete(example);
    }
}

