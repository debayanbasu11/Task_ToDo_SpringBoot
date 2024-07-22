package com.debayan.taskmanagement.entities;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.debayan.taskmanagement.dto.TaskDto;
import com.debayan.taskmanagement.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String description;
	
	private Date dueDate;
	
	private String priority;
	
	private TaskStatus taskStatus;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;
	
	public TaskDto getTaskDto() {
		TaskDto taskDto = new TaskDto();
		taskDto.setId(id);
		taskDto.setTitle(title);
		taskDto.setDescription(description);
		taskDto.setEmployeeName(user.getName());
		taskDto.setEmployeeId(user.getId());
		taskDto.setTaskStatus(taskStatus);
		taskDto.setDueDate(dueDate);
		taskDto.setPriority(priority);
		
		return taskDto;
	}
}
