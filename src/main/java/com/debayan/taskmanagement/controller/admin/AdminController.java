package com.debayan.taskmanagement.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.debayan.taskmanagement.dto.CommentDto;
import com.debayan.taskmanagement.dto.TaskDto;
import com.debayan.taskmanagement.services.admin.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

	private final AdminService adminService;
	
	@GetMapping("/users")
	public ResponseEntity<?> getUsers(){
		return ResponseEntity.ok(adminService.getUsers());
	}
	
	@PostMapping("/task")
	public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto){
		TaskDto createdTaskDto = adminService.createTask(taskDto);
		if(createdTaskDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
	}
	
	@GetMapping("/tasks")
	public ResponseEntity<?> getAllTasks(){
		return ResponseEntity.ok(adminService.getAllTasks());
	}
	
	@DeleteMapping("/task/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		adminService.deleteTask(id);
		return (ResponseEntity<Void>) ResponseEntity.ok();
	}
	
	@GetMapping("/task/{id}")
	public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id){
		return ResponseEntity.ok(adminService.getTaskById(id));
	}
	
	@PutMapping("/task/{id}")
	public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto){
		TaskDto updatedTask = adminService.updateTask(id, taskDto);
		if(updatedTask == null) return ResponseEntity.notFound().build();
		return ResponseEntity.ok(updatedTask);
	}
	
	@GetMapping("/tasks/search/{title}")
	public ResponseEntity<List<TaskDto>> searchTask(@PathVariable String title){
		return ResponseEntity.ok(adminService.searchTaskByTitle(title));
	}
	
	@PostMapping("/task/comment/{taskId}")
	public ResponseEntity<CommentDto> createComment(@PathVariable Long taskId, @RequestParam String content){
		CommentDto createdCommentDto = adminService.createComment(taskId, content);
		if(createdCommentDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
	}
	
	@GetMapping("/comments/{taskId}")
	public ResponseEntity<List<CommentDto>> getCommentsByTaskId(@PathVariable Long taskId){
		return ResponseEntity.ok(adminService.getCommentsByTaskId(taskId));
	}
}
