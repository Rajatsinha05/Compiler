package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.Student.StudentDTO;
import com.Code.Compiler.DTO.StudentSolvedQuestionsDTO;
import com.Code.Compiler.Service.Implementation.StudentServiceImpl;
import com.Code.Compiler.models.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentServiceImpl studentService;

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get a student by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable("id") Long id) {
        Optional<StudentDTO> student = studentService.getStudentById(id);
        return student.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    // Update a student by ID
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudentDetails(id, studentDTO);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    // Delete a student by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get the solved questions of a student
    @GetMapping("/{id}/solved-questions")
    public ResponseEntity<StudentSolvedQuestionsDTO> getStudentSolvedQuestions(@PathVariable("id") Long id) {
        StudentSolvedQuestionsDTO studentSolvedQuestions = studentService.getStudentSolvedQuestions(id);
        return new ResponseEntity<>(studentSolvedQuestions, HttpStatus.OK);
    }

//    get all students
    @GetMapping("/all")
    public ResponseEntity<List<Students>> getAllStudentswithData(){
        return new ResponseEntity<>(studentService.getAll(), HttpStatus.OK);
    }
}
