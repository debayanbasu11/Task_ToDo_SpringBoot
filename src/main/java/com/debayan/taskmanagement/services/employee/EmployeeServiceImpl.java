package com.debayan.taskmanagement.services.employee;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.debayan.taskmanagement.dto.CommentDto;
import com.debayan.taskmanagement.dto.TaskDto;
import com.debayan.taskmanagement.entities.Comment;
import com.debayan.taskmanagement.entities.Task;
import com.debayan.taskmanagement.entities.User;
import com.debayan.taskmanagement.enums.TaskStatus;
import com.debayan.taskmanagement.repositories.CommentRepository;
import com.debayan.taskmanagement.repositories.TaskRepository;
import com.debayan.taskmanagement.utils.JwtUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

	private final TaskRepository taskRepository;
	
	private final JwtUtil jwtUtil;
	
	private final CommentRepository commentRepository;

	@Override
	public List<TaskDto> getTaskByUserId() {
		User user = jwtUtil.getLoggedInUser();
		
		if(user != null) {
			return taskRepository.findAllByUserId(user.getId())
			.stream()
			.sorted(Comparator.comparing(Task::getDueDate).reversed())
			.map(Task::getTaskDto)
			.collect(Collectors.toList());
		}
		
		throw new EntityNotFoundException("User not found!");
	}

	@Override
	public TaskDto updateTask(Long id, String status) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent()) {
			Task existingTask = optionalTask.get();
			existingTask.setTaskStatus(mapStringToTaskStatus(status));
			return taskRepository.save(existingTask).getTaskDto();
		}
		throw new EntityNotFoundException("Task not found!");
	}
	
	private TaskStatus mapStringToTaskStatus(String status) {
		return switch(status) {
		case "PENDING" -> TaskStatus.PENDING;
		case "INPROGRESS" -> TaskStatus.INPROGRESS;
		case "COMPLETED" -> TaskStatus.COMPLETED;
		case "DEFERRED" -> TaskStatus.DEFERRED;
		default -> TaskStatus.CANCELLED;
		};
	}
	
	@Override
	public TaskDto getTaskById(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		
		return optionalTask.map(Task::getTaskDto).orElse(null);
	}
	
	@Override
	public CommentDto createComment(Long taskId, String content) {
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		User user = jwtUtil.getLoggedInUser();
		if(optionalTask.isPresent() && user != null) {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setCreatedAt(new Date());
			comment.setTask(optionalTask.get());
			comment.setUser(user);
			return commentRepository.save(comment).getCommentDto();
			
		}
		throw new EntityNotFoundException("User or Task Not Found!");
	}

	@Override
	public List<CommentDto> getCommentsByTaskId(Long taskId) {
		
		return commentRepository.findAllByTaskId(taskId)
				.stream()
				.map(Comment::getCommentDto)
				.collect(Collectors.toList());
	}
}
