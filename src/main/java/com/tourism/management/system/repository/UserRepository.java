package com.tourism.management.system.repository;

import com.tourism.management.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Method to find a user by their username
    User findByUsername(String username);
}
