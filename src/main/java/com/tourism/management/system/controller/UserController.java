package com.tourism.management.system.controller;

import com.tourism.management.system.model.User;
import com.tourism.management.system.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint to register a user (signup)
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Validate user input for username and password
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return new ResponseEntity<>("Username cannot be empty", HttpStatus.BAD_REQUEST);
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return new ResponseEntity<>("Password cannot be empty", HttpStatus.BAD_REQUEST);
            }

            // Check if the username already exists
            if (userService.existsByUsername(user.getUsername())) {
                return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
            }

            // Save the user to the database
            userService.saveUser(user);

            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to fetch all users
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            // Fetch all users from the database
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching users", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to remove a user by username
    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> removeUser(@PathVariable String username) {
        try {
            // Check if the user exists before deletion
            if (userService.existsByUsername(username)) {
                userService.deleteUserByUsername(username);
                return new ResponseEntity<>("User removed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while removing the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
 // Endpoint to update the role of a user
    @PutMapping("/users/{username}/role")
    public ResponseEntity<String> updateRole(@PathVariable String username, @RequestBody Map<String, String> roleData) {
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                user.setRole(roleData.get("role"));
                userService.saveUser(user); // Save the updated user to the database
                return new ResponseEntity<>("User role updated successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update user role", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
