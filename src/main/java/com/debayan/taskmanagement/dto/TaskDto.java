package com.debayan.taskmanagement.dto;

import java.util.Date;

import com.debayan.taskmanagement.enums.TaskStatus;

import lombok.Data;

@Data
public class TaskDto {

	private Long id;
	
	private String title;
	
	private String description;
	
	private Date dueDate;
	
	private String priority;
	
	private TaskStatus taskStatus;
	
	private Long employeeId;
	
	private String employeeName;
	
	
}
