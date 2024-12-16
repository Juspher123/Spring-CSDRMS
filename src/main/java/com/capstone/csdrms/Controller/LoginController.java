package com.capstone.csdrms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.capstone.csdrms.Entity.UserEntity;
import com.capstone.csdrms.Methods.LoginRequest;
import com.capstone.csdrms.Service.LoginService;
import com.capstone.csdrms.Jwt.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class LoginController {
	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtTokenUtil jwtUtil;

	@Autowired
	LoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							username, password));

			UserEntity user = (UserEntity) authentication.getPrincipal();
			String accessToken = jwtUtil.generateAccessToken(user);

			user = loginService.login(username, password);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}
