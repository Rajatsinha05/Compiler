package com.Code.Compiler.Mapper;

import com.Code.Compiler.DTO.UserDTO;
import com.Code.Compiler.models.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
dto.setRole(user.getRole());
dto.setDepartment(user.getDepartment());

        return dto;
    }
}
