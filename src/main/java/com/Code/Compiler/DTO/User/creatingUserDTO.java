package com.Code.Compiler.DTO.User;

import com.Code.Compiler.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class creatingUserDTO {
        private String name;
        private String email;
        private String department;
        private Role role;
        private String password;
}
