package com.Code.Compiler.Service.Implementation;
import com.Code.Compiler.Exceptions.UserNotFoundException;
import com.Code.Compiler.Repository.StudentRepository;
import com.Code.Compiler.Repository.UserRepository;
import com.Code.Compiler.Service.Interfaces.IUserService;
import com.Code.Compiler.models.Students;
import com.Code.Compiler.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user, List<Long> studentIds) {
        List<Students> students = studentRepository.findAllById(studentIds);
        user.setStudents(students);
        return userRepository.save(user);
    }

    public User updateUserDetails(Long id, User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setName(userDetails.getName());
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
