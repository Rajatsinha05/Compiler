package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.LoginRequest;
import com.Code.Compiler.DTO.User.creatingUserDTO;
import com.Code.Compiler.DTO.UserWithToken;
import com.Code.Compiler.Enum.Permission;
import com.Code.Compiler.Exceptions.AlreadyExists;
import com.Code.Compiler.Exceptions.UserNotFoundException;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.Service.Interfaces.IUserService;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StudentRepository studentRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserWithToken createUser(@Valid User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new AlreadyExists("User already exists");
        }

        if (user.getPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setId(ThreadLocalRandom.current().nextLong(1_000_000_000_000L, Long.MAX_VALUE));
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getUsername());
        String userDetailsToken = jwtService.generateToken(savedUser.toString());

        return new UserWithToken(userDetailsToken, token);
    }

    public UserWithToken loginUser(@Valid LoginRequest loginRequest) {
        User existingUser = userRepository.findByEmail(loginRequest.getEmail());
        if (existingUser == null) {
            throw new ValidationException("User not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new ValidationException("Invalid password");
        }

        String token = jwtService.generateToken(existingUser.getUsername());
        String userDetailsToken = jwtService.generateToken(existingUser.toString());

        return new UserWithToken(userDetailsToken, token);
    }

    public User updateUserDetails(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        existingUser.setDepartment(userDetails.getDepartment());
        existingUser.setRole(userDetails.getRole());

        return userRepository.save(existingUser);
    }

    public User associateStudentsWithUser(Long userId, List<Long> studentIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<Students> students = studentRepository.findAllById(studentIds);
        user.setStudents(students);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public  User createUserByAdmin( User user) {
        return userRepository.save(user);

    }



    // Add multiple permissions to a user
    public User addPermissionsToUser(Long userId, List<Permission> permissions) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Add permissions if they are not already present
        for (Permission permission : permissions) {
            if (!user.getPermissions().contains(permission)) {
                user.getPermissions().add(permission);
            }
        }
        return userRepository.save(user);
    }

    // Remove multiple permissions from a user
    public User removePermissionsFromUser(Long userId, List<Permission> permissions) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Remove permissions if they exist
        for (Permission permission : permissions) {
            user.getPermissions().remove(permission);
        }
        return userRepository.save(user);
    }

}
