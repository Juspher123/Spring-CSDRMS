package com.capstone.csdrms.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {

	@Autowired
    UserService userService;
	
	@PostMapping("/registerUser")
    public String registerSSO(@RequestBody UserEntity user) {
		userService.register(user);
        return "user created successfully";
    }

     
//    @GetMapping("/getAllAdvisers")
//    public List<AdviserEntity> getAllAdvisers() {
//        return userService.getAllAdvisers();
//    }
//    
    @GetMapping("/getAllUsers")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long userId, @RequestBody UserEntity updatedUser) {
        
        UserEntity updatedUserEntity = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(updatedUserEntity);
    }
    
    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content response on successful deletion
    }  
    
    
//    @GetMapping("/adviser")
//    public ResponseEntity<AdviserEntity> getAdviser(@RequestParam int grade,@RequestParam String section,@RequestParam String schoolYear) {
//    	 Optional<AdviserEntity> adviser = userService.getAdviser(grade, section, schoolYear);
//         
//         if (adviser.isPresent()) {
//             return ResponseEntity.ok(adviser.get());
//         } else {
//             return ResponseEntity.notFound().build();
//         }
//    }
}
