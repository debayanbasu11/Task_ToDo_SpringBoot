package com.debayan.taskmanagement.services.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.debayan.taskmanagement.dto.SignupRequest;
import com.debayan.taskmanagement.dto.UserDto;
import com.debayan.taskmanagement.entities.User;
import com.debayan.taskmanagement.enums.UserRole;
import com.debayan.taskmanagement.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	@Autowired
	public UserRepository userRepository;
	
	@PostConstruct
	public void createAnAdminAccount() {
		Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
		if(optionalUser.isEmpty()) {
			User user = new User();
			user.setEmail("admin@test.com");
			user.setName("admin");
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			user.setUserRole(UserRole.ADMIN);
			userRepository.save(user);
			System.out.println("Admin account created successfully!");
		}else {
			System.out.println("Admin account already exists!");
		}
	}

	@Override
	public UserDto signupUser(SignupRequest signupRequest) {
		User user = new User();
		user.setEmail(signupRequest.getEmail());
		user.setName(signupRequest.getName());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		user.setUserRole(UserRole.EMPLOYEE);
		User createdUser = userRepository.save(user);
		
		return createdUser.getUserDto();
	}

	@Override
	public boolean hasUserWithEmail(String email) {
		
		return userRepository.findFirstByEmail(email).isPresent();
	}
}
