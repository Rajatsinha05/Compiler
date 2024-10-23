package com.Code.Compiler.Controllers;

import com.Code.Compiler.DTO.LoginRequest;
import com.Code.Compiler.DTO.UserDTO;
import com.Code.Compiler.DTO.UserWithToken;
import com.Code.Compiler.Enum.Permission;
import com.Code.Compiler.Exceptions.AlreadyExists;
import com.Code.Compiler.Service.Implementation.LoginServiceImpl;
import com.Code.Compiler.Service.Implementation.UserService;
import com.Code.Compiler.models.User;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
   private LoginServiceImpl loginService;
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            UserWithToken newUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User successfully created.");
        } catch (AlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists.");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the user.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserWithToken> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.loginUser(loginRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUserDetails(id, userDetails);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}/students")
    public ResponseEntity<User> associateStudentsWithUser(@PathVariable Long userId, @RequestParam List<Long> studentIds) {
        User updatedUser = userService.associateStudentsWithUser(userId, studentIds);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/createUser")
    public ResponseEntity<User> createUserBySuperAdmin(@RequestBody User user){

        return  ResponseEntity.status(HttpStatus.CREATED).body(userService.createUserByAdmin(user));
    }


//    @PreAuthorize("hasRole('SUPERADMIN')")
@PutMapping("/{userId}/permissions/add")
public ResponseEntity<User> addPermissionsToUser(
        @PathVariable Long userId,
        @RequestBody List<Permission> permissions // Accept list of permissions
) {
    User updatedUser = userService.addPermissionsToUser(userId, permissions);
    return ResponseEntity.ok(updatedUser);
}

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PutMapping("/{userId}/permissions/remove")
    public ResponseEntity<User> removePermissionsFromUser(
            @PathVariable Long userId,
            @RequestBody List<Permission> permissions // Accept list of permissions
    ) {
        User updatedUser = userService.removePermissionsFromUser(userId, permissions);
        return ResponseEntity.ok(updatedUser);
    }

}
