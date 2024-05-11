package com.Code.Compiler.Controllers;

import com.Code.Compiler.Service.CodeExecutionService;
import com.Code.Compiler.models.CodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CodeController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    private String lastOutput;

    @PostMapping("/submit")
    public ResponseEntity<String> submitCode(@RequestBody CodeRequest codeRequest) {
        String code = codeRequest.getCode();
        String language = codeRequest.getLanguage();
        String inputData = codeRequest.getInputData(); // New

        try {
            lastOutput = codeExecutionService.compileAndRunCode(code, language, inputData); // Modified
            return ResponseEntity.ok("Code submitted and executed successfully!"+ lastOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error executing code: " + e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<String> getOutput() {
        if (lastOutput != null) {
            return ResponseEntity.ok(lastOutput);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No output available");
        }
    }
}
