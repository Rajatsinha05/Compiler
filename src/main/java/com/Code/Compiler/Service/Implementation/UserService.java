package com.Code.Compiler.Service.Implementation;
import com.Code.Compiler.DTO.LoginRequest;
import com.Code.Compiler.DTO.UserDTO;
import com.Code.Compiler.DTO.UserWithToken;
import com.Code.Compiler.Exceptions.AlreadyExists;
import com.Code.Compiler.Exceptions.UserNotFoundException;
import com.Code.Compiler.Mapper.UserMapper;
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
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Autowired
    private StudentRepository studentRepository;

    public List<User> getAllUsers() {
//        return userRepository.findAll().stream()
//                .map(UserMapper::toDTO)
//                .collect(Collectors.toList());

        return  userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserWithToken createUser(@Valid User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new AlreadyExists("User already exists");
        }
        String password = user.getPassword();
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);

        // Generate JWT token for the new user
        String token = jwtService.generateToken(savedUser.getUsername());
        String userToken = savedUser.toString();
        String userDetailsToken= jwtService.generateToken(userToken);

        // Return user and token
        return new UserWithToken(userDetailsToken, token);
    }

    //    login user
    public UserWithToken loginUser(@Valid LoginRequest loginRequest) {
        User existingUser = userRepository.findByEmail(loginRequest.getEmail());
        if (existingUser == null) {
            throw new ValidationException("User not found");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new ValidationException("Invalid password");
        }
        String token= jwtService.generateToken(existingUser.getUsername());
        String userToken =existingUser.toString();
        String userDetailsToken= jwtService.generateToken(userToken);

        // Return user and token
        return new UserWithToken(userDetailsToken, token);

    }


    public User updateUserDetails(Long id, User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setName(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPassword(userDetails.getPassword());
            existingUser.setDepartment(userDetails.getDepartment());
            existingUser.setRole(userDetails.getRole());
            return userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    public User associateStudentsWithUser(Long userId, List<Long> studentIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Students> students = studentRepository.findAllById(studentIds);
            user.setStudents(students);
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}