package com.Code.Compiler.Service.Implementation;



import com.Code.Compiler.DTO.QuestionDTO;
import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.DTO.StudentSolvedQuestionsDTO;
import com.Code.Compiler.Exceptions.QuestionNotFoundException;
import com.Code.Compiler.Exceptions.StudentNotFoundException;
import com.Code.Compiler.Mapper.StudentMapper;
import com.Code.Compiler.Repository.QuestionsRepository;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.Service.Interfaces.IStudentService;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.Students;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private QuestionsRepository questionRepository;
    @Autowired
    public StudentServiceImpl(UserRepository userRepository, JwtService jwtService) {

        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public List<StudentDTO> getAllStudents() {
        List<Students> students = studentRepository.findAll();
        return students.stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Students> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Students createStudent(Students student) {

        String password = student.getPassword();
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        student.setPassword(passwordEncoder.encode(password));
        return studentRepository.save(student);
    }

    public Students updateStudentDetails(Long id, Students studentDetails) {
        Optional<Students> student = studentRepository.findById(id);
        if (student.isPresent()) {
            Students existingStudent = student.get();
            existingStudent.setName(studentDetails.getUsername());
            existingStudent.setEmail(studentDetails.getEmail());
            existingStudent.setPassword(studentDetails.getPassword());
            existingStudent.setGrid(studentDetails.getGrid());
            existingStudent.setCourse(studentDetails.getCourse());
            return studentRepository.save(existingStudent);
        } else {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
    }
    public Students addSolvedQuestion(Long studentId, Long questionId) {
        Optional<Students> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new StudentNotFoundException("Student not found with id: " + studentId);
        }

        Students student = studentOptional.get();

        Optional<Questions> questionOptional = questionRepository.findById(questionId);
        if (!questionOptional.isPresent()) {
            throw new QuestionNotFoundException("Question not found with id: " + questionId);
        }

        Questions question = questionOptional.get();

        // Add the question to the student's solved questions list
        student.getSolvedQuestions().add(question);

        // Save the updated student details
        return studentRepository.save(student);
    }

    public StudentSolvedQuestionsDTO getStudentSolvedQuestions(Long studentId) {
        Optional<Students> studentOptional = studentRepository.findById(studentId);
        if (!studentOptional.isPresent()) {
            throw new StudentNotFoundException("Student not found with id: " + studentId);
        }

        Students student = studentOptional.get();
        Set<Long> solvedQuestionIds = new HashSet<>();
        List<QuestionDTO> solvedQuestions = student.getSolvedQuestions().stream()
                .filter(question -> solvedQuestionIds.add(question.getId()))  // Only add if not already present
                .map(question -> new QuestionDTO(question.getId(), question.getTitle()))
                .collect(Collectors.toList());

        return new StudentSolvedQuestionsDTO(student.getName(), solvedQuestions);
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }


}

