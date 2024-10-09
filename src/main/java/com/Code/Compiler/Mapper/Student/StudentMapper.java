package com.Code.Compiler.Mapper.Student;

import com.Code.Compiler.DTO.Student.StudentDTO;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.Enum.Role;

public class StudentMapper {

    // Convert Students entity to StudentDTO
    public static StudentDTO toDto(Students student) {
        if (student == null) {
            return null;
        }

        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getGrid(),
                student.getCourse(),
                student.getBranchCode(),
                student.getRole(), // Role is an enum and can be directly used here
                null // Do not expose password in DTO
        );
    }

    // Convert StudentDTO to Students entity
    public static Students toEntity(StudentDTO studentDTO) {
        if (studentDTO == null) {
            return null;
        }

        Students student = new Students();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setGrid(studentDTO.getGrid());
        student.setCourse(studentDTO.getCourse());
        student.setBranchCode(studentDTO.getBranchCode());

        // Set the role if available; otherwise, use the default
        if (studentDTO.getRole() != null) {
            student.setRole(studentDTO.getRole());
        } else {
            student.setRole(Role.STUDENT); // Default role if not provided
        }

        // Set the password
        student.setPassword(studentDTO.getPassword());

        return student;
    }
}
