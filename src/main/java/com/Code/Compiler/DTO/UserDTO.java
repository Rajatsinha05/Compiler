package com.Code.Compiler.DTO;
import com.Code.Compiler.Enum.Role;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String department;
    private Role role;
}


