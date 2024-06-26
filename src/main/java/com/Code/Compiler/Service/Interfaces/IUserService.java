package com.Code.Compiler.Service.Interfaces;


import com.Code.Compiler.DTO.UserDTO;
import com.Code.Compiler.DTO.UserWithToken;
import com.Code.Compiler.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    UserWithToken createUser(User user);

    User updateUserDetails(Long id, User userDetails);

    User associateStudentsWithUser(Long userId, List<Long> studentIds);

    void deleteUser(Long id);
}
