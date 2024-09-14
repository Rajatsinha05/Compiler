package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.StudentDTO;
import com.Code.Compiler.models.Students;

public class StudentMapper {

    public static StudentDTO toDTO(Students student) {
        if (student == null) {
            return null;
        }

        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getGrid(),
                student.getCourse(),
                student.getBranchCode()
        );
    }
}
