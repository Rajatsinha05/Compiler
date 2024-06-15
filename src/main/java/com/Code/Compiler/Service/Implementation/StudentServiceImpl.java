package com.Code.Compiler.Service.Implementation;



import com.Code.Compiler.Exceptions.StudentNotFoundException;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.Service.Interfaces.IStudentService;
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
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository, JwtService jwtService) {

        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public List<Students> getAllStudents() {
        return studentRepository.findAll();
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

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }


}

