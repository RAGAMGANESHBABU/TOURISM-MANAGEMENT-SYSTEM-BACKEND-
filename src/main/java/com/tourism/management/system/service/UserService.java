package com.tourism.management.system.service;

import com.tourism.management.system.model.User;
import com.tourism.management.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to save a new user
    public void saveUser(User user) {
        userRepository.save(user);  // This will persist the user entity to the database
    }

    // Method to check if the username already exists
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // Method to find a user by their username (for login)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Method to delete a user by username
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user); // Deletes the user by their username
        }
    }

    // Method to fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Fetch all users from the database
    }
}
