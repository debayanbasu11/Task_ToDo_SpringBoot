package com.debayan.taskmanagement.services.admin;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.debayan.taskmanagement.dto.CommentDto;
import com.debayan.taskmanagement.dto.TaskDto;
import com.debayan.taskmanagement.dto.UserDto;
import com.debayan.taskmanagement.entities.Comment;
import com.debayan.taskmanagement.entities.Task;
import com.debayan.taskmanagement.entities.User;
import com.debayan.taskmanagement.enums.TaskStatus;
import com.debayan.taskmanagement.enums.UserRole;
import com.debayan.taskmanagement.repositories.CommentRepository;
import com.debayan.taskmanagement.repositories.TaskRepository;
import com.debayan.taskmanagement.repositories.UserRepository;
import com.debayan.taskmanagement.utils.JwtUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	
	private final UserRepository userRepository;
	private final TaskRepository taskRepository;
	private final CommentRepository commentRepository;
	private final JwtUtil jwtUtil;
	
	@Override
	public List<UserDto> getUsers() {
		return userRepository.findAll()
				.stream()
				.filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
				.map(User::getUserDto)
				.collect(Collectors.toList());
	}

	@Override
	public TaskDto createTask(TaskDto taskDto) {
		Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
		if(optionalUser.isPresent()) {
			Task task = new Task();
			task.setTitle(taskDto.getTitle());
			task.setDescription(taskDto.getDescription());
			task.setPriority(taskDto.getPriority());
			task.setDueDate(taskDto.getDueDate());
			task.setTaskStatus(TaskStatus.INPROGRESS);
			task.setUser(optionalUser.get());
			
			return taskRepository.save(task).getTaskDto();
		}
		return null;
	}

	@Override
	public List<TaskDto> getAllTasks() {
		
		return taskRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(Task::getDueDate).reversed())
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteTask(Long id) {
		
		taskRepository.deleteById(id);
	}

	@Override
	public TaskDto getTaskById(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		
		return optionalTask.map(Task::getTaskDto).orElse(null);
	}

	@Override
	public TaskDto updateTask(Long id, TaskDto taskDto) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
		if(optionalTask.isPresent() && optionalUser.isPresent()) {
			Task existingTask = optionalTask.get();
			existingTask.setTitle(taskDto.getTitle());
			existingTask.setDescription(taskDto.getDescription());
			existingTask.setDueDate(taskDto.getDueDate());
			existingTask.setPriority(taskDto.getPriority());
			existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDto.getTaskStatus())));
			existingTask.setUser(optionalUser.get());
			return taskRepository.save(existingTask).getTaskDto();
		}
		return null;
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
	public List<TaskDto> searchTaskByTitle(String title) {
		
		return taskRepository.findAllByTitleContaining(title)
				.stream()
				.sorted(Comparator.comparing(Task::getDueDate).reversed())
				.map(Task::getTaskDto)
				.collect(Collectors.toList());
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
