package com.debayan.taskmanagement.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.debayan.taskmanagement.dto.TaskDto;
import com.debayan.taskmanagement.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findAllByTitleContaining(String title);

	List<Task> findAllByUserId(Long id);

}
