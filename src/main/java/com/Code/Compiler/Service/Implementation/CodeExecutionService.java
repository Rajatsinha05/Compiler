package com.Code.Compiler.Service.Implementation;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

@Service
public class CodeExecutionService {

    private static final int TIMEOUT_SECONDS = 25; // Set the time limit for execution

    public String compileAndRunCode(String code, String language, String inputData) {
        try {
            switch (language) {
                case "java":
                    return compileAndRunJava(code, inputData);
                case "javascript":
                    return runJavaScript(code, inputData);
                case "c":
                    return compileAndRunC(code, inputData);
                case "cpp":
                    return compileAndRunCPP(code, inputData);
                case "python":
                    return runPython(code, inputData);
                case "dart":
                    return runDart(code, inputData);
                case "typescript":
                    return runTypeScript(code, inputData);
                case "csharp":
                    return runCSharp(code, inputData);
                case "php":
                    return runPhp(code, inputData);
                case "kotlin":
                    return compileAndRunKotlin(code, inputData);
                default:
                    return "Unsupported language";
            }
        } finally {
            cleanupFiles(); // Clean up temporary files
        }
    }

    private String compileAndRunJava(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.java"), code.getBytes());

            Process compileProcess = new ProcessBuilder("javac", "Main.java").start();
            compileProcess.waitFor();
            if (compileProcess.exitValue() != 0) {
                return readProcessOutput(compileProcess.getErrorStream());
            }

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("java", "Main").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        }
    }

    private String runJavaScript(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.js"), code.getBytes());

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("node", "Main.js").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String compileAndRunC(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.c"), code.getBytes());

            Process compileProcess = new ProcessBuilder("gcc", "-o", "Main", "Main.c").start();
            compileProcess.waitFor();
            if (compileProcess.exitValue() != 0) {
                return readProcessOutput(compileProcess.getErrorStream());
            }

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("./Main").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        }
    }

    private String compileAndRunCPP(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.cpp"), code.getBytes());

            Process compileProcess = new ProcessBuilder("g++", "-o", "Main", "Main.cpp").start();
            compileProcess.waitFor();
            if (compileProcess.exitValue() != 0) {
                return readProcessOutput(compileProcess.getErrorStream());
            }

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("./Main").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        }
    }

    private String runPython(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.py"), code.getBytes());

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("python3", "Main.py").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String runDart(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.dart"), code.getBytes());

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("dart", "Main.dart").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String runTypeScript(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.ts"), code.getBytes());

            Process compileProcess = new ProcessBuilder("tsc", "Main.ts").start();
            compileProcess.waitFor();
            if (compileProcess.exitValue() != 0) {
                return readProcessOutput(compileProcess.getErrorStream());
            }

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("node", "Main.js").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        }
    }

    private String runCSharp(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.cs"), code.getBytes());

            Process compileProcess = new ProcessBuilder("csc", "/out:Main.exe", "Main.cs").start();
            compileProcess.waitFor();
            if (compileProcess.exitValue() != 0) {
                return readProcessOutput(compileProcess.getErrorStream());
            }

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("mono", "Main.exe").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        }
    }

    private String runPhp(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.php"), code.getBytes());

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("php", "Main.php").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    private String compileAndRunKotlin(String code, String inputData) {
        try {
            Files.write(Paths.get("Main.kt"), code.getBytes());

            Process compileProcess = new ProcessBuilder("kotlinc", "Main.kt", "-include-runtime", "-d", "Main.jar").start();
            compileProcess.waitFor();
            if (compileProcess.exitValue() != 0) {
                return readProcessOutput(compileProcess.getErrorStream());
            }

            return executeWithTimeout(() -> {
                Process execProcess = new ProcessBuilder("java", "-jar", "Main.jar").start();
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }
                return readProcessOutput(execProcess);
            });
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        }
    }

    private String executeWithTimeout(Callable<String> task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(task);
        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return "Error: Time Limit Exceeded (TLE)";
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Error: " + e.getMessage();
        } finally {
            executor.shutdownNow();
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

    private String readProcessOutput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
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
            Files.deleteIfExists(Paths.get("Main.ts"));
            Files.deleteIfExists(Paths.get("Main.cs"));
            Files.deleteIfExists(Paths.get("Main.php"));
            Files.deleteIfExists(Paths.get("Main.kt"));
            Files.deleteIfExists(Paths.get("Main.jar"));
            Files.deleteIfExists(Paths.get("Main"));
            Files.deleteIfExists(Paths.get("Main.exe"));
        } catch (IOException e) {
            System.err.println("Error cleaning up files: " + e.getMessage());
        }
    }
}
