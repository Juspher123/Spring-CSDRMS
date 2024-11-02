package com.capstone.csdrms.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.UserRepository;

@Service
public class LoginService {
	
	@Autowired
	UserRepository userRepository;
	
	
	public UserEntity login(String username, String password) {

        // Retrieve user by username
        Optional<UserEntity> user = userRepository.findByUsernameAndDeleted(username,false);

        // Create a BCryptPasswordEncoder for password comparison
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        // Check if the user exists and the password matches
        if (user.isPresent() && bcrypt.matches(password, user.get().getPassword())) {
            // Login successful, return the user entity
            return user.get();
        } else {
            // Login failed, throw an exception
            throw new RuntimeException("Invalid username or password");
        }
    }
}
