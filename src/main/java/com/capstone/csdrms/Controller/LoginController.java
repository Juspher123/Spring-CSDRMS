package com.capstone.csdrms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Methods.LoginRequest;
import com.capstone.csdrms.Service.LoginService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class LoginController {
	
	
	@Autowired
	LoginService loginService;
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
	    String username = loginRequest.getUsername();
	    String password = loginRequest.getPassword();

	    // Call the login method in LoginService
	    try {
	        UserEntity user = loginService.login(username, password);
	        return new ResponseEntity<>(user, HttpStatus.OK);
	    } catch (RuntimeException e) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }
	 }
}
