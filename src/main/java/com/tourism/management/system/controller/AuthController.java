package com.tourism.management.system.controller;

import com.tourism.management.system.model.User;
import com.tourism.management.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        try {
            // Validate input
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Username cannot be empty");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Password cannot be empty");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Check if the user exists in the database
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Invalid username");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Check if the password is correct
            if (!existingUser.getPassword().equals(user.getPassword())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Invalid password");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Successful login, include the role in the response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("role", existingUser.getRole());  // Include the role of the user
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "An error occurred during login");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
