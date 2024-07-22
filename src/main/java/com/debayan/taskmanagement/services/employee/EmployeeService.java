package com.debayan.taskmanagement.services.employee;

import java.util.List;

import com.debayan.taskmanagement.dto.CommentDto;
import com.debayan.taskmanagement.dto.TaskDto;

public interface EmployeeService {

	List<TaskDto> getTaskByUserId();
	
	TaskDto updateTask(Long id, String status);
	
	TaskDto getTaskById(Long id);
	
	CommentDto createComment(Long taskId, String content);
	
	List<CommentDto> getCommentsByTaskId(Long taskId);
}
