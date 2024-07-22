package com.debayan.taskmanagement.services.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.debayan.taskmanagement.repositories.UserRepository;
import com.debayan.taskmanagement.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetailsService userDetailService() {
		
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
				return userRepository.findFirstByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
			}
		};
	}

}
