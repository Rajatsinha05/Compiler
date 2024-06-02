package com.Code.Compiler.Service.Implementation;



import com.Code.Compiler.Exceptions.StudentNotFoundException;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Service.Interfaces.IStudentService;
import com.Code.Compiler.models.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Students> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Students> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Students createStudent(Students student) {
        return studentRepository.save(student);
    }

    public Students updateStudentDetails(Long id, Students studentDetails) {
        Optional<Students> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Students existingStudent = student.get();
            existingStudent.setName(studentDetails.getName());
            existingStudent.setEmail(studentDetails.getEmail());
            existingStudent.setPassword(studentDetails.getPassword());
            existingStudent.setGrid(studentDetails.getGrid());
            existingStudent.setCourse(studentDetails.getCourse());
            return studentRepository.save(existingStudent);
        } else {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Service
    public static class CodeExecutionService {

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
                if (compileProcess.exitValue() != 0) {
                    return readProcessOutput(compileProcess.getErrorStream());
                }

                // Execute compiled Java code
                Process execProcess = new ProcessBuilder("java", "Main").start();

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

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

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

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
                if (compileProcess.exitValue() != 0) {
                    return readProcessOutput(compileProcess.getErrorStream());
                }

                // Execute compiled C code
                Process execProcess = new ProcessBuilder("./Main").start();

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

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
                if (compileProcess.exitValue() != 0) {
                    return readProcessOutput(compileProcess.getErrorStream());
                }

                // Execute compiled C++ code
                Process execProcess = new ProcessBuilder("./Main").start();

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

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
                Process execProcess = new ProcessBuilder("python3", "Main.py").start();

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

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

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

                return readProcessOutput(execProcess);
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        private String runTypeScript(String code, String inputData) {
            try {
                // Save the TypeScript code to a file
                Files.write(Paths.get("Main.ts"), code.getBytes());

                // Compile TypeScript code to JavaScript
                Process compileProcess = new ProcessBuilder("tsc", "Main.ts").start();
                compileProcess.waitFor();
                if (compileProcess.exitValue() != 0) {
                    return readProcessOutput(compileProcess.getErrorStream());
                }

                // Execute compiled JavaScript code with Node.js
                Process execProcess = new ProcessBuilder("node", "Main.js").start();

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

                return readProcessOutput(execProcess);
            } catch (IOException | InterruptedException e) {
                return "Error: " + e.getMessage();
            }
        }

        private String runCSharp(String code, String inputData) {
            try {
                // Save the C# code to a file
                Files.write(Paths.get("Main.cs"), code.getBytes());

                // Compile C# code
                Process compileProcess = new ProcessBuilder("csc", "/out:Main.exe", "Main.cs").start();
                compileProcess.waitFor();
                if (compileProcess.exitValue() != 0) {
                    return readProcessOutput(compileProcess.getErrorStream());
                }

                // Execute compiled C# code
                Process execProcess = new ProcessBuilder("mono", "Main.exe").start();

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

                return readProcessOutput(execProcess);
            } catch (IOException | InterruptedException e) {
                return "Error: " + e.getMessage();
            }
        }

        private String runPhp(String code, String inputData) {
            try {
                // Save the PHP code to a file
                Files.write(Paths.get("Main.php"), code.getBytes());

                // Execute PHP code
                Process execProcess = new ProcessBuilder("php", "Main.php").start();

                // Redirect input stream if inputData is provided
                if (inputData != null && !inputData.isEmpty()) {
                    execProcess.getOutputStream().write(inputData.getBytes());
                    execProcess.getOutputStream().close();
                }

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
                Files.deleteIfExists(Paths.get("Main"));
                Files.deleteIfExists(Paths.get("Main.exe"));
            } catch (IOException e) {
                System.err.println("Error cleaning up files: " + e.getMessage());
            }
        }
    }
}

