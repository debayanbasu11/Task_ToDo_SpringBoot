package com.debayan.taskmanagement.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.debayan.taskmanagement.dto.UserDto;
import com.debayan.taskmanagement.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	private String email;
	
	private String password;
	
	private UserRole userRole;
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return List.of(new SimpleGrantedAuthority(userRole.name()));
	}

	@Override
	public String getUsername() {
		
		return email;
	}
	
	public UserDto getUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(id);
		userDto.setEmail(email);
		userDto.setName(name);
		userDto.setUserRole(userRole);
		
		return userDto;
	}

}
