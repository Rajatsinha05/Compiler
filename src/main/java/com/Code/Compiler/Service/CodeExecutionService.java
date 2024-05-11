package com.Code.Compiler.Service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CodeExecutionService {

    public String compileAndRunCode(String code, String language, String inputData) {
        try {
            switch (language) {
                case "java":
                    return compileAndRunJava(code, inputData);
                case "js":
                    return runJavaScript(code, inputData);
                case "c":
                    return compileAndRunC(code, inputData);
                case "cpp":
                    return compileAndRunCPP(code, inputData);
                case "python":
                    return runPython(code, inputData);
                case "dart":
                    return runDart(code, inputData);
                default:
                    return "Unsupported language";
            }
        } finally {
            // Clean up: Remove temporary files
            cleanupFiles();
        }
    }

    private String compileAndRunJava(String code, String inputData) {
        try {
            // Save the Java code to a file
            Files.write(Paths.get("Main.java"), code.getBytes());

            // Compile Java code
            Process compileProcess = new ProcessBuilder("javac", "Main.java").start();
            compileProcess.waitFor();

            // Execute compiled Java code
            Process execProcess = new ProcessBuilder("java", "Main").start();

            // Write input data to the process
            execProcess.getOutputStream().write(inputData.getBytes());
            execProcess.getOutputStream().close();

            return readProcessOutput(execProcess);
        } catch (IOException | InterruptedException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String runJavaScript(String code, String inputData) {
        try {
            // Save the JavaScript code to a file
            Files.write(Paths.get("Main.js"), code.getBytes());

            // Execute JavaScript code with Node.js
            Process execProcess = new ProcessBuilder("node", "Main.js").start();

            // Write input data to the process
            execProcess.getOutputStream().write(inputData.getBytes());
            execProcess.getOutputStream().close();

            return readProcessOutput(execProcess);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String compileAndRunC(String code, String inputData) {
        try {
            // Save the C code to a file
            Files.write(Paths.get("Main.c"), code.getBytes());

            // Compile C code
            Process compileProcess = new ProcessBuilder("gcc", "-o", "Main", "Main.c").start();
            compileProcess.waitFor();

            // Execute compiled C code
            Process execProcess = new ProcessBuilder("./Main").start();

            // Write input data to the process
            execProcess.getOutputStream().write(inputData.getBytes());
            execProcess.getOutputStream().close();

            return readProcessOutput(execProcess);
        } catch (IOException | InterruptedException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String compileAndRunCPP(String code, String inputData) {
        try {
            // Save the C++ code to a file
            Files.write(Paths.get("Main.cpp"), code.getBytes());

            // Compile C++ code
            Process compileProcess = new ProcessBuilder("g++", "-o", "Main", "Main.cpp").start();
            compileProcess.waitFor();

            // Execute compiled C++ code
            Process execProcess = new ProcessBuilder("./Main").start();

            // Write input data to the process
            execProcess.getOutputStream().write(inputData.getBytes());
            execProcess.getOutputStream().close();

            return readProcessOutput(execProcess);
        } catch (IOException | InterruptedException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String runPython(String code, String inputData) {
        try {
            // Save the Python code to a file
            Files.write(Paths.get("Main.py"), code.getBytes());

            // Execute Python code
            Process execProcess = new ProcessBuilder("python", "Main.py").start();

            // Write input data to the process
            execProcess.getOutputStream().write(inputData.getBytes());
            execProcess.getOutputStream().close();

            return readProcessOutput(execProcess);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String runDart(String code, String inputData) {
        try {
            // Save the Dart code to a file
            Files.write(Paths.get("Main.dart"), code.getBytes());

            // Execute Dart code
            Process execProcess = new ProcessBuilder("dart", "Main.dart").start();

            // Write input data to the process
            execProcess.getOutputStream().write(inputData.getBytes());
            execProcess.getOutputStream().close();

            return readProcessOutput(execProcess);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String readProcessOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    private void cleanupFiles() {
        try {
            Files.deleteIfExists(Paths.get("Main.java"));
            Files.deleteIfExists(Paths.get("Main.class"));
            Files.deleteIfExists(Paths.get("Main.js"));
            Files.deleteIfExists(Paths.get("Main.c"));
            Files.deleteIfExists(Paths.get("Main.cpp"));
            Files.deleteIfExists(Paths.get("Main.py"));
            Files.deleteIfExists(Paths.get("Main.dart"));
            Files.deleteIfExists(Paths.get("Main"));
        } catch (IOException e) {
            System.err.println("Error cleaning up files: " + e.getMessage());
        }
    }
}
