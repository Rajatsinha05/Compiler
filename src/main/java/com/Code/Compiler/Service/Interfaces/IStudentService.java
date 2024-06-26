package com.Code.Compiler.Service.Interfaces;



import com.Code.Compiler.models.Students;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
    List<Students> getAllStudents();

    Optional<Students> getStudentById(Long id);

    Students createStudent(Students student);

    Students updateStudentDetails(Long id, Students studentDetails);

    void deleteStudent(Long id);
}
