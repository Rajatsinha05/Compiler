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

@Service
public class LoginServiceImpl {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository, StudentRepository studentRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

//    public UserWithToken loginUser(@Valid LoginRequest loginRequest) {
//        User existingUser = userRepository.findByEmail(loginRequest.getEmail());
//        Students existingStudent = studentRepository.findByEmail(loginRequest.getEmail());
//
//        if (existingUser != null) {
//            if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
//                throw new ValidationException("Invalid password");
//            }
//            String token = jwtService.generateToken(existingUser.toString());
//            return new UserWithToken(existingUser.toString(), token);
//        } else if (existingStudent != null) {
//            if (!passwordEncoder.matches(loginRequest.getPassword(), existingStudent.getPassword())) {
//                throw new ValidationException("Invalid password");
//            }
//            String token = jwtService.generateToken(existingStudent.toString());
//            return new UserWithToken(existingStudent.toString(), token);
//        } else {
//            throw new ValidationException("User not found: " + loginRequest.getEmail());
//        }
//    }
public UserWithToken loginUser(@Valid LoginRequest loginRequest) {
    User existingUser = userRepository.findByEmail(loginRequest.getEmail());
        Students existingStudent = studentRepository.findByEmail(loginRequest.getEmail());
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
}
