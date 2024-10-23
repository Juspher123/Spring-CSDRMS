package com.capstone.csdrms.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Service
public class UserService {


	@Autowired
	UserRepository userRepository;
	
	
	
	 @PersistenceContext
	 private EntityManager entityManager;
	
	 public void register(UserEntity user) {

		 Optional<UserEntity> existingUser = userRepository.findByUsername(user.getUsername());

	        if (existingUser.isPresent()) {
	            throw new IllegalArgumentException("Username already exist");
	        }

	        if (user.getUserType()== 3) {
	        	
	        	Optional<UserEntity> adviserUser = userRepository.findByGradeAndSectionAndSchoolYear(user.getGrade(), user.getSection(), user.getSchoolYear());
	          

	            if (adviserUser.isPresent()) {
	            	throw new IllegalArgumentException("Adviser with the same grade, section, and school year already exists");
	            }
	        } 
	        else {
	        	user.setGrade(null);
	        	user.setSection(null);
	        	user.setSchoolYear(null);
	        }

	        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	        String encryptedPassword = bcrypt.encode(user.getPassword());
	        user.setPassword(encryptedPassword);

	       
	            userRepository.save(user);
	    }
	
	public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
	
	
	public UserEntity updateUser(Long userId, UserEntity updatedUser) {
		Optional<UserEntity> optionalUser = userRepository.findById(userId);
		
		 if (optionalUser.isPresent()) {
	            UserEntity existingUser = optionalUser.get();
	            
	            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
	    	        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
	    	        String encryptedPassword = bcrypt.encode(updatedUser.getPassword());
	    	        existingUser.setPassword(encryptedPassword);
	    	    }
	            
	            // Update fields based on the input user object
	            existingUser.setUsername(updatedUser.getUsername());
	            existingUser.setFirstname(updatedUser.getFirstname());
	            existingUser.setLastname(updatedUser.getLastname());;
	            
	            if(existingUser.getUserType() == 3) {
	            	existingUser.setGrade(updatedUser.getGrade());
		            existingUser.setSection(updatedUser.getSection());
		            existingUser.setSchoolYear(updatedUser.getSchoolYear());
	            }
	            

	            // Save the updated user back to the database
	            return userRepository.save(existingUser);
	        } else {
	            // Handle case where user is not found
	            throw new RuntimeException("User not found with id: " + userId);
	        }

	}
	
	 public void deleteUser(String username) {
	        // Find the user by their username
	        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

	        if (optionalUser.isPresent()) {
	            // If the user is found, delete the user
	            UserEntity user = optionalUser.get();
	            userRepository.delete(user);
	        } else {
	            // Handle case where user is not found
	            throw new RuntimeException("User not found with username: " + username);
	        }
	    }
	 
//	 public Optional<AdviserEntity> getAdviser(int grade, String section, String schoolYear) {
//		 return adviserRepository.findByGradeAndSectionAndSchoolYear(grade, section, schoolYear);
//	 }
	
	
	
}
