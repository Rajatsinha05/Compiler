package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.Student.StudentDTO;
import com.Code.Compiler.DTO.QuestionDTO;
import com.Code.Compiler.DTO.StudentSolvedQuestionsDTO;
import com.Code.Compiler.Enum.Role;
import com.Code.Compiler.Exceptions.QuestionNotFoundException;
import com.Code.Compiler.Exceptions.StudentNotFoundException;
import com.Code.Compiler.Mapper.Student.StudentMapper;
import com.Code.Compiler.Repository.QuestionsRepository;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.models.Questions;
import com.Code.Compiler.models.Students;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuestionsRepository questionRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Get all students as DTOs
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());
    }

    // Get a student by ID as a DTO
    public Optional<StudentDTO> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(StudentMapper::toDto);
    }

    // Create a student from StudentDTO
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Check if student with the same email or grid already exists
        if (studentRepository.existsByEmail(studentDTO.getEmail()) || studentRepository.existsByGrid(studentDTO.getGrid())) {
            throw new ValidationException("Student with the same email or grid already exists");
        }

        Students student = StudentMapper.toEntity(studentDTO);

        String password = student.getPassword();
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        student.setPassword(passwordEncoder.encode(password));
        Students savedStudent = studentRepository.save(student);

        return StudentMapper.toDto(savedStudent);
    }

    // Update student details using StudentDTO
    public StudentDTO updateStudentDetails(Long id, StudentDTO studentDTO) {
        Optional<Students> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Students existingStudent = studentOptional.get();
            existingStudent.setName(studentDTO.getName());
            existingStudent.setEmail(studentDTO.getEmail());
            existingStudent.setGrid(studentDTO.getGrid());
            existingStudent.setCourse(studentDTO.getCourse());
            existingStudent.setBranchCode(studentDTO.getBranchCode());
            if (studentDTO.getPassword() != null && studentDTO.getPassword().length() >= 8) {
                existingStudent.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
            }
            existingStudent.setRole(studentDTO.getRole() != null ? studentDTO.getRole() : Role.STUDENT);

            Students updatedStudent = studentRepository.save(existingStudent);
            return StudentMapper.toDto(updatedStudent);
        } else {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
    }

    // Get student's solved questions
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

    // Delete a student by ID
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    // Get all students
    public List<Students> getAll() {
        return studentRepository.findAll();
    }
}
