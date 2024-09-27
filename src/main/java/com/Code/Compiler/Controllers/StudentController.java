package com.Code.Compiler.Controllers;



import com.Code.Compiler.DTO.StudentSolvedQuestionsDTO;
import com.Code.Compiler.DTO.StudentsDTO;
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

    @GetMapping
    public ResponseEntity<List<StudentsDTO>> getAllStudents() {
        List<StudentsDTO> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Students> getStudentById(@PathVariable("id") Long id) {
        Optional<Students> student = studentService.getStudentById(id);
        return student.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Students> createStudent(@RequestBody Students student) {
        Students createdStudent = studentService.createStudent(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Students> updateStudent(@PathVariable("id") Long id, @RequestBody Students studentDetails) {
        Students updatedStudent = studentService.updateStudentDetails(id, studentDetails);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{studentId}/solve/{questionId}")
    public ResponseEntity<Students> addSolvedQuestion(@PathVariable("studentId") Long studentId,
                                                      @PathVariable("questionId") Long questionId) {
        Students updatedStudent = studentService.addSolvedQuestion(studentId, questionId);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }
    @GetMapping("/{id}/solved-questions")
    public ResponseEntity<StudentSolvedQuestionsDTO> getStudentSolvedQuestions(@PathVariable("id") Long id) {
        StudentSolvedQuestionsDTO studentSolvedQuestions = studentService.getStudentSolvedQuestions(id);
        return new ResponseEntity<>(studentSolvedQuestions, HttpStatus.OK);
    }
}
