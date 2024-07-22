package com.debayan.taskmanagement.services.admin;

import java.util.List;

import com.debayan.taskmanagement.dto.CommentDto;
import com.debayan.taskmanagement.dto.TaskDto;
import com.debayan.taskmanagement.dto.UserDto;

public interface AdminService {

	List<UserDto> getUsers();
	
	TaskDto createTask(TaskDto taskDto);
	
	List<TaskDto> getAllTasks();
	
	void deleteTask(Long id);
	
	TaskDto getTaskById(Long id);
	
	TaskDto updateTask(Long id, TaskDto taskDto);
	
	List<TaskDto> searchTaskByTitle(String title);
	
	CommentDto createComment(Long taskId, String content);
	
	List<CommentDto> getCommentsByTaskId(Long taskId);
}
