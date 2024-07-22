package com.debayan.taskmanagement.controller.employee;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.debayan.taskmanagement.dto.CommentDto;
import com.debayan.taskmanagement.dto.TaskDto;
import com.debayan.taskmanagement.services.employee.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
@CrossOrigin("*")
public class EmployeeController {

	private final EmployeeService employeeService;
	
	@GetMapping("/tasks")
	public ResponseEntity<List<TaskDto>> getTasksByUserId(){
		return ResponseEntity.ok(employeeService.getTaskByUserId());
	}
	
	@GetMapping("/task/{id}/{status}")
	public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @PathVariable String status){
		TaskDto updatedTaskDto = employeeService.updateTask(id, status);
		if(updatedTaskDto == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.ok(updatedTaskDto);
	}
	
	@GetMapping("/task/{id}")
	public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id){
		return ResponseEntity.ok(employeeService.getTaskById(id));
	}
	
	@PostMapping("/task/comment/{taskId}")
	public ResponseEntity<CommentDto> createComment(@PathVariable Long taskId, @RequestParam String content){
		CommentDto createdCommentDto = employeeService.createComment(taskId, content);
		if(createdCommentDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
	}
	
	@GetMapping("/comments/{taskId}")
	public ResponseEntity<List<CommentDto>> getCommentsByTaskId(@PathVariable Long taskId){
		return ResponseEntity.ok(employeeService.getCommentsByTaskId(taskId));
	}
}
