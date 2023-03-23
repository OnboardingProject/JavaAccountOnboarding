package com.account.onboarding.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure of the Task details of a project to store in user
 *              entity.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetails {
	private Integer taskId;
	private String assignedBy;
	private String updatedBy;
	private LocalDateTime assignedTime;
	private LocalDateTime updateTime;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private List<Documents> taskDocuments;
	private String taskName;
	private String taskStatus;
}