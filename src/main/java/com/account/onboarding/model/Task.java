package com.account.onboarding.model;

import java.util.List;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Structure for storing task details to a project.
 * @date : 08 August 2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	@Transient
	public static final String SEQUENCE_NAME = "Task_Sequence";
	private Integer taskId;
	private String taskName;
	private String taskDesc;
	private String taskType;
	private int completionDays;
	private List<Documents> taskDocuments;
	private String designation;
}
