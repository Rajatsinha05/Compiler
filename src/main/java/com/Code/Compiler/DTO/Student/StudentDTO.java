package com.Code.Compiler.DTO.Student;

import com.Code.Compiler.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private String grid;
    private String course;
    private String branchCode;
    private Role role;
    private  String password;


}
