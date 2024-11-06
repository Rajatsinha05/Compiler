package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.CodeRequestDto;
import com.Code.Compiler.Service.Implementation.CodeExecutionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.*;
import java.util.concurrent.*;

@RestController
@CrossOrigin(origins = "*")
public class CodeController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    private final BlockingQueue<CodeRequestDto> requestQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Scalable thread pool for high load
    private final ExecutorService timeoutExecutor = Executors.newCachedThreadPool(); // Scalable thread pool for timeouts
    private final Map<String, Map<String, String>> resultStore = new ConcurrentHashMap<>(); // Store structured results by request ID

    public CodeController() {
        startQueueProcessor();
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitCode(@RequestBody CodeRequestDto codeRequest) {
        String requestId = UUID.randomUUID().toString();
        codeRequest.setRequestId(requestId);
        try {
            requestQueue.put(codeRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Your request is queued and will be processed shortly");
            response.put("requestId", requestId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to queue request: " + e.getMessage()));
        }
    }

    private void startQueueProcessor() {
        executorService.submit(() -> {
            while (true) {
                try {
                    CodeRequestDto codeRequest = requestQueue.take();
                    Map<String, String> result = processRequest(codeRequest);
                    resultStore.put(codeRequest.getRequestId(), result);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private Map<String, String> processRequest(CodeRequestDto codeRequest) {
        Future<Map<String, String>> future = timeoutExecutor.submit(() -> {
            String code = codeRequest.getCode();
            String language = codeRequest.getLanguage();
            String inputData = codeRequest.getInputData();

            long startTime = System.nanoTime();
            MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage beforeUsedMemory = memoryMXBean.getHeapMemoryUsage();

            Map<String, String> resultDetails = new HashMap<>();
            try {
                String output = codeExecutionService.compileAndRunCode(code, language, inputData);

                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

                MemoryUsage afterUsedMemory = memoryMXBean.getHeapMemoryUsage();
                long memoryUsed = (afterUsedMemory.getUsed() - beforeUsedMemory.getUsed()) / 1024; // Convert to KB

                resultDetails.put("output", output);
                resultDetails.put("executionTime", String.valueOf(duration));
                resultDetails.put("memoryUsed", String.valueOf(memoryUsed));
                resultDetails.put("status", "success");
                return resultDetails;
            } catch (Exception e) {
                resultDetails.put("status", "error");
                resultDetails.put("output", "Error executing code: " + e.getMessage());
                return resultDetails;
            }
        });

        try {
            return future.get(50000, TimeUnit.MILLISECONDS); // Set timeout to 5000 ms (5 seconds)
        } catch (TimeoutException e) {
            future.cancel(true);
            Map<String, String> timeoutResponse = new HashMap<>();
            timeoutResponse.put("status", "error");
            timeoutResponse.put("output", "Time Limit Exceeded (TLE): Code execution took too long");
            return timeoutResponse;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("output", "Error executing code: " + e.getMessage());
            return errorResponse;
        }
    }

    @GetMapping("/result/{requestId}")
    public ResponseEntity<Map<String, Object>> getResult(@PathVariable String requestId) {
        int maxAttempts = 50; // Maximum number of attempts
        int attempt = 0;
        int delay = 1000; // Delay between attempts in milliseconds (1 second)

        while (attempt < maxAttempts) {
            if (resultStore.containsKey(requestId)) {
                Map<String, String> result = resultStore.get(requestId);
                Map<String, Object> response = new HashMap<>();
                response.put("requestId", requestId);
                response.put("status", result.get("status"));
                response.put("output", result.get("output"));
                response.put("executionTime", result.get("executionTime") + " ms");
                response.put("memoryUsed", result.get("memoryUsed") + " KB");
                return ResponseEntity.ok(response);
            }
            try {
                Thread.sleep(delay); // Wait before the next check
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error", "Server was interrupted while waiting for the result"));
            }
            attempt++;
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "Result not available or still processing"));
    }

    @GetMapping("/")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("working");
    }

    // Inner class to hold the result details
    @Getter
    @Setter
    private static class Result {
        private final String status;
        private final String output;
        private final long executionTime;
        private final long memoryUsed;

        public Result(String status, String output, long executionTime, long memoryUsed) {
            this.status = status;
            this.output = output;
            this.executionTime = executionTime;
            this.memoryUsed = memoryUsed;
        }
    }
}