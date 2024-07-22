package com.debayan.taskmanagement.services.auth;

import com.debayan.taskmanagement.dto.SignupRequest;
import com.debayan.taskmanagement.dto.UserDto;

public interface AuthService {

	UserDto signupUser(SignupRequest signupRequest);

	boolean hasUserWithEmail(String email);
}
