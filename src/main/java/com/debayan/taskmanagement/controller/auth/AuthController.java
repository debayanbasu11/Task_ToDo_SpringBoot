package com.debayan.taskmanagement.controller.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.debayan.taskmanagement.dto.AuthenticationRequest;
import com.debayan.taskmanagement.dto.AuthenticationResponse;
import com.debayan.taskmanagement.dto.SignupRequest;
import com.debayan.taskmanagement.dto.UserDto;
import com.debayan.taskmanagement.entities.User;
import com.debayan.taskmanagement.repositories.UserRepository;
import com.debayan.taskmanagement.services.UserService;
import com.debayan.taskmanagement.services.auth.AuthService;
import com.debayan.taskmanagement.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private final AuthService authService;
	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final JwtUtil jwtUtil;
	
	@Autowired
	private final UserService userService;
	
	@Autowired
	private final AuthenticationManager authenticationManager;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
		if(authService.hasUserWithEmail(signupRequest.getEmail()))
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this email id!");
	
		UserDto createdUserDto = authService.signupUser(signupRequest);
		if(createdUserDto == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created!");
	
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody AuthenticationRequest  authenticationRequest) {
		
		try {
			Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(),
					authenticationRequest.getPassword()));
			
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect username or password!");
		}
		
		final UserDetails userDetails = userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
		
		Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());
		
		final String jwtToken = jwtUtil.generateToken(userDetails);
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		if(optionalUser.isPresent()) {
			authenticationResponse.setJwt(jwtToken);
			authenticationResponse.setUserId(optionalUser.get().getId());
			authenticationResponse.setUserRole(optionalUser.get().getUserRole());
		}
		return authenticationResponse;
	}
}
