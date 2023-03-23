package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.request.DeleteTaskRequest;
import com.account.onboarding.request.ProjectTaskRequest;
import com.account.onboarding.response.ResponsePayLoad;
import com.account.onboarding.service.impl.ProjectTasksService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sheeba VR
 * @description : Controller class for fetch the task details based on project.
 * @date : 10 August 2022
 */

@Slf4j
@Validated
@RestController
@RequestMapping(ProjectOnboardingConstant.PROJECT_TASKS_MAPPING)
public class ProjectTasksController {

	@Autowired
	private ProjectTasksService projectTasksService;

	/**
	 * Description : API for fetch task details based on project
	 * 
	 * @Param : projectId
	 * @Return: List of Task object
	 * 
	 */
	@GetMapping(ProjectOnboardingConstant.GET_PROJECT_TASKS_BY_PROJECT_ID)
	@Operation(summary = ProjectOnboardingConstant.GET_PROJECT_TASKS_BY_PROJECT_ID_SUMMARY, description = ProjectOnboardingConstant.GET_PROJECT_TASKS_BY_PROJECT_ID_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> getAllTaskByProject(@PathVariable String projectId) {
	
			log.info("Started project task fetch api method");
			List<Object> tasksList = new ArrayList<Object>();
			tasksList.addAll(projectTasksService.getProjectTasksByProjectId(projectId));

			log.info("Return the selected project task details");

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(tasksList, ProjectOnboardingConstant.API_TASK_LIST_FETCH_SUCCESS, ""),
					HttpStatus.OK);

	}

	/**
	 * Description : API for Add/Edit task of a project
	 * 
	 * @Param : projectTaskRequest
	 * @Return: Project
	 */
	@PostMapping(ProjectOnboardingConstant.ADD_EDIT_TASK)
	@Operation(summary = ProjectOnboardingConstant.ADD_EDIT_TASK_SUMMARY, description = ProjectOnboardingConstant.ADD_EDIT_TASK_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> addOrEditTask(@RequestBody ProjectTaskRequest projectTaskRequest) {
		log.info("In Add or Edit Task controller");

		List<Object> newProject = new ArrayList<Object>();
		newProject.add(projectTasksService.addOrEditTask(projectTaskRequest));

		log.info("Task is added or edited successsfully");
		return new ResponseEntity<ResponsePayLoad>(new ResponsePayLoad(newProject, "SucessFully Tasks Added", " "),
				HttpStatus.CREATED);

	}

	/**
	 * Description : API for delete the task based on project
	 * 
	 * @Param : DeleteTaskRequest
	 * @Return: List of Task object
	 * 
	 */
	@PutMapping(ProjectOnboardingConstant.DELETE_TASK)
	@Operation(summary = ProjectOnboardingConstant.DELETE_TASK_SUMMARY, description = ProjectOnboardingConstant.DELETE_TASK_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> deleteTaskByProject(
			@Valid @RequestBody DeleteTaskRequest deleteTaskRequest) {
	
			log.info("Started project task delete api method");
			List<Object> tasksList = new ArrayList<Object>();
			tasksList.add(projectTasksService.deleteTask(deleteTaskRequest));

			log.info("Return the selected project task details");
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(tasksList, ProjectOnboardingConstant.API_DELETE_TASKS_SUCCESS, ""),
					HttpStatus.OK);
	
	}
}
