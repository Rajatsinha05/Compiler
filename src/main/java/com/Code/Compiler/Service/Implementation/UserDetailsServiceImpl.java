package com.Code.Compiler.Service.Implementation;

import com.Code.Compiler.Exceptions.UserNotFoundException;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByEmail(username);
        Students student = studentRepository.findByEmail(username);

        logger.info("Username: {}", username);

        if (user != null) {
            logger.info("User: {}", user);
            // Initialize lazy collections
            user.getQuestions().size();
            user.getExamples().size();
            user.getStudents().size();
            return user;
        } else if (student != null) {
            logger.info("Student: {}", student);
            student.getUsers().size();
            student.getEnrolledContests().size();
            student.getAttemptedContests().size();
            student.getSolvedQuestions().size();
            return student;
        } else {
            throw new UserNotFoundException("User not found with email: " + username);
        }
    }
}
