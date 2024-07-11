package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.DTO.LoginRequest;
import com.Code.Compiler.DTO.UserWithToken;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserWithToken loginUser(@Valid LoginRequest loginRequest) {
        logger.info("Attempting to log in user with email: {}", loginRequest.getEmail());

        User existingUser = userRepository.findByEmail(loginRequest.getEmail());
        Students existingStudent = studentRepository.findByEmail(loginRequest.getEmail());

        if (existingUser != null) {
            if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
                logger.warn("Invalid password for user: {}", loginRequest.getEmail());
                throw new ValidationException("Invalid password");
            }
            String token = jwtService.generateToken(existingUser.getUsername());
            return new UserWithToken(existingUser.toString(), token);

        } else if (existingStudent != null) {
            if (!passwordEncoder.matches(loginRequest.getPassword(), existingStudent.getPassword())) {
                logger.warn("Invalid password for student: {}", loginRequest.getEmail());
                throw new ValidationException("Invalid password");
            }
            String token = jwtService.generateToken(existingStudent.getUsername());
            return new UserWithToken(existingStudent.toString(), token);

        } else {
            logger.warn("User not found with email: {}", loginRequest.getEmail());
            throw new ValidationException("User not found: " + loginRequest.getEmail());
        }
    }
}
