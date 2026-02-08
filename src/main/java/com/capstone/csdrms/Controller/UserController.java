package com.capstone.csdrms.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/registerUser")
    public String registerSSO(@RequestBody UserEntity user) {
        userService.register(user);
        return "user created successfully";
    }

    // @GetMapping("/getAllAdvisers")
    // public List<AdviserEntity> getAllAdvisers() {
    // return userService.getAllAdvisers();
    // }
    //
    @GetMapping("/getAllUsers")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/updateUser/{userId}/{initiator}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long userId, @RequestBody UserEntity updatedUser,
            @PathVariable Long initiator) {

        UserEntity updatedUserEntity = userService.updateUser(userId, updatedUser, initiator);
        return ResponseEntity.ok(updatedUserEntity);
    }

    // @DeleteMapping("/deleteUser/{username}")
    // public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    // userService.deleteUser(username);
    // return ResponseEntity.noContent().build(); // HTTP 204 No Content response on
    // successful deletion
    // }

    @DeleteMapping("/deleteUser/{username}/{initiator}")
    public ResponseEntity<String> softDeleteUser(@PathVariable String username, @PathVariable Long initiator) {
        boolean deleted = userService.softDeleteUserByUsername(username, initiator);
        if (deleted) {
            return ResponseEntity.ok("User marked as deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    @GetMapping("/getPrincipal")
    public ResponseEntity<UserEntity> getPrincipal() {
        try {
            UserEntity principal = userService.getPrincipal();
            return ResponseEntity.ok(principal);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // or include a custom error message if needed
        }
    }

    @GetMapping("/adviser")
    public ResponseEntity<UserEntity> getAdviser(@RequestParam int grade, @RequestParam String section,
            @RequestParam String schoolYear) {
        Optional<UserEntity> adviser = userService.getAdviser(grade, section, schoolYear);

        if (adviser.isPresent()) {
            return ResponseEntity.ok(adviser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
