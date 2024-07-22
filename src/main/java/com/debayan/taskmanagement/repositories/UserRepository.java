package com.debayan.taskmanagement.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.debayan.taskmanagement.entities.User;
import com.debayan.taskmanagement.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findFirstByEmail(String username);

	Optional<User> findByUserRole(UserRole role);

}
