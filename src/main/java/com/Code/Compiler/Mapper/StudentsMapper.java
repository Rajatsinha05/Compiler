package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.StudentsDTO;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.Questions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentsMapper {

    public StudentsDTO toDto(Students student) {
        if (student == null) {
            return null;
        }

        List<String> solvedQuestionsTitles = student.getSolvedQuestions()
                .stream()
                .map(Questions::getTitle)
                .collect(Collectors.toList());

        return new StudentsDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getGrid(),
                student.getCourse(),
                student.getBranchCode(),
                student.getRole().name(),
                solvedQuestionsTitles
        );
    }

    public Students toEntity(StudentsDTO studentsDTO) {
        if (studentsDTO == null) {
            return null;
        }

        Students student = new Students();
        student.setId(studentsDTO.getId());
        student.setName(studentsDTO.getName());
        student.setEmail(studentsDTO.getEmail());
        student.setGrid(studentsDTO.getGrid());
        student.setCourse(studentsDTO.getCourse());
        student.setBranchCode(studentsDTO.getBranchCode());

        return student;
    }
}
