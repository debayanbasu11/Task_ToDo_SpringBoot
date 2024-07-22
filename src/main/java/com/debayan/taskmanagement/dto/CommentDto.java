package com.debayan.taskmanagement.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDto {

	private Long id;
	private String content;
	private Date createdAt;
	private Long taskId;
	private Long userId;
	private String postedBy;
	
}
