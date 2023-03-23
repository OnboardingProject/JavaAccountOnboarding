package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.request.SaveTaskStatusRequest;
import com.account.onboarding.response.ResponsePayLoad;
import com.account.onboarding.service.impl.ProjectOnboardingService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Amrutha Joseph
 * @description controller class for project onboarding
 * @created_Date 17/08/2022
 */

@Slf4j
@RestController
@RequestMapping(ProjectOnboardingConstant.PROJECT_ONBOARDING)
public class ProjectOnboardingController {

	@Autowired
	private ProjectOnboardingService projectOnboardingService;

	/**
	 * @param userId
	 * @return ResponseEntity<List<ProjectDetails>>
	 * @description : Fetch all projects based on user
	 */

	@GetMapping(ProjectOnboardingConstant.ONBOARDING_BY_USER_ID)
	@Operation(summary = ProjectOnboardingConstant.ONBOARDING_BY_USER_ID_SUMMARY, description = ProjectOnboardingConstant.ONBOARDING_BY_USER_ID_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> getProjects(@PathVariable String userId) {
	
			log.info("Project onboarding controller getProjects with {}", userId);
			List<Object> projects = new ArrayList<Object>();
			projects.addAll(projectOnboardingService.getProjectsBasedOnUser(userId));
			log.info("Project onboarding controller getProjects ends with {}", userId);
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(projects, ProjectOnboardingConstant.API_GET_PROJECTS_SUCCESS, ""),
					HttpStatus.FOUND);
			
	}

	/**
	 * @param projectId
	 * @return ResponseEntity<List<UserDetails>>
	 * @description : Fetch all resources based on project
	 */

	@GetMapping(ProjectOnboardingConstant.ONBOARDING_BY_PROJECT_ID)
	@Operation(summary = ProjectOnboardingConstant.ONBOARDING_BY_PROJECT_ID_SUMMARY, description = ProjectOnboardingConstant.ONBOARDING_BY_PROJECT_ID_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> getUsers(@PathVariable String projectId) {


			log.info("Project onboarding controller getUsers starts with projectId {}", projectId);
			List<Object> users = new ArrayList<Object>();
			users.addAll(projectOnboardingService.getUsersBasedOnProject(projectId));

			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(users, ProjectOnboardingConstant.API_GET_RESOURCES_SUCCESS, ""),
					HttpStatus.FOUND);

			
	}

	/**
	 * @param projectId, resourceId
	 * @return ResponseEntity<TaskDetails>
	 * @description : Show Task List associated with Project and Resource
	 */

	@GetMapping(ProjectOnboardingConstant.VIEW_TASK)
	@Operation(summary = ProjectOnboardingConstant.VIEW_TASK_SUMMARY, description = ProjectOnboardingConstant.VIEW_TASK_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> getAllTasks(@PathVariable String projectId,
			@PathVariable String resourceId) {
			log.info("ProjectOnboardingController view tasks starts with resourceId {}", resourceId);

			List<Object> allTasks = new ArrayList<Object>();
			allTasks.addAll(projectOnboardingService.fetchTaskList(projectId, resourceId));
			log.info("ProjectOnboardingController view tasks ends with resourceId {}", resourceId);
			return new ResponseEntity<ResponsePayLoad>(
					new ResponsePayLoad(allTasks, ProjectOnboardingConstant.API_TASK_LIST_FETCH_SUCCESS, ""),
					HttpStatus.OK);

	}

	/**
	 * @param projectId, userId, taskId, taskStatus
	 * @return ResponseEntity<TaskDetails>
	 * @description : Save Task Status based on User and project Tasks.
	 */

	@PutMapping(ProjectOnboardingConstant.SAVE_TASK_STATUS)
	@Operation(summary = ProjectOnboardingConstant.SAVE_TASK_STATUS_SUMMARY, description = ProjectOnboardingConstant.SAVE_TASK_STATUS_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> saveTaskStatus(@RequestBody SaveTaskStatusRequest saveTaskStatusRequest) {
		log.info("ProjectOnboardingController save-task-status starts with {}", saveTaskStatusRequest);

		List<Object> taskDetailsList = new ArrayList<Object>();
		taskDetailsList.addAll(projectOnboardingService.saveStatus(saveTaskStatusRequest));
		log.info("ProjectOnboardingController save-task-status ends with {}", saveTaskStatusRequest);
		return new ResponseEntity<ResponsePayLoad>(
				new ResponsePayLoad(taskDetailsList, ProjectOnboardingConstant.API_TASK_STATUS_SAVE_SUCCESS, ""),
				HttpStatus.OK);
	}

	/**
	 * Description : API for fetch task status
	 * 
	 * @param
	 * @return: List of Types object, ProjectOnboardingException, Exception
	 */
	@GetMapping(ProjectOnboardingConstant.FETCH_TASK_STATUS)
	@Operation(summary = ProjectOnboardingConstant.FETCH_TASK_STATUS_SUMMARY, description = ProjectOnboardingConstant.FETCH_TASK_STATUS_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> fetchAllTaskStatus() {

		log.info("ProjectOnboardingController fetch all task status starts");

		List<Object> statusList = new ArrayList<Object>();
		statusList.addAll(projectOnboardingService.getAllTaskStatus());

		log.info("ProjectOnboardingController fetch all task status ends");
		return new ResponseEntity<ResponsePayLoad>(
				new ResponsePayLoad(statusList, ProjectOnboardingConstant.API_TASK_STATUS_FETCH_SUCCESS, ""),
				HttpStatus.OK);

	}
}
