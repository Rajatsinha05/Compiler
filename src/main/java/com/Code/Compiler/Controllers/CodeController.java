package com.Code.Compiler.Controllers;

import com.Code.Compiler.Service.Implementation.CodeExecutionService;
import com.Code.Compiler.Service.Implementation.StudentServiceImpl;
import com.Code.Compiler.models.CodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class CodeController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    private String lastOutput;
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitCode(@RequestBody CodeRequest codeRequest) {
        String code = codeRequest.getCode();
        String language = codeRequest.getLanguage();
        String inputData = codeRequest.getInputData();

        try {
            lastOutput = codeExecutionService.compileAndRunCode(code, language, inputData);

            Map<String, String> response = new HashMap<>();
            response.put("output", lastOutput);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error executing code: " + e.getMessage()));
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

    @GetMapping("/")
    public ResponseEntity<String> get(){
        return ResponseEntity.ok("working");
    }
}
