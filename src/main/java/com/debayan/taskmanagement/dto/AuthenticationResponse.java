package com.debayan.taskmanagement.dto;

import com.debayan.taskmanagement.enums.UserRole;

import lombok.Data;

@Data
public class AuthenticationResponse {

	
	private String jwt;
	private Long userId;
	private UserRole userRole;
	
}
