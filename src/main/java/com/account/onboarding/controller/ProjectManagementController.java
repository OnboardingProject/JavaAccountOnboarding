package com.account.onboarding.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectManagementConstant;
import com.account.onboarding.entity.Project;
import com.account.onboarding.request.ProjectRequest;
import com.account.onboarding.response.UserListResponse;
import com.account.onboarding.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is the controller class for all the ProjectManagement API end
 * points Exposes CRUD endpoints for Project resource
 */
@Slf4j
@RestController
@RequestMapping(ProjectManagementConstant.PROJECT)
public class ProjectManagementController {

	@Autowired
	private ProjectService projectService;

	/**
	 * This method will create a new project and also validate incoming request
	 * 
	 * @param project
	 * @return response entity representation of project
	 */
	@PostMapping(ProjectManagementConstant.ADD_PROJECT)
	@Operation(summary = ProjectManagementConstant.ADD_PROJECT_SUMMARY, description = ProjectManagementConstant.ADD_PROJECT_DESCRIPTION)
	public ResponseEntity<ProjectRequest> addProject(@Valid @RequestBody ProjectRequest project) {
		log.info(ProjectManagementConstant.PJT_CONST_START);
		ProjectRequest pjtDTO = projectService.createProject(project);
		log.info(ProjectManagementConstant.PJT_CONST_END);
		return new ResponseEntity<ProjectRequest>(pjtDTO, HttpStatus.CREATED);
	}

	/**
	 * THis method will will list all the users under the resource role
	 * 
	 * @return response entity representation of collection of Users
	 */
	@GetMapping(ProjectManagementConstant.GET_RESOURCES)
	@Operation(summary = ProjectManagementConstant.GET_USERS_SUMMARY, description = ProjectManagementConstant.GET_USERS_DESCRIPTION)
	public ResponseEntity<List<UserListResponse>> getAllUsers() {
		log.info(ProjectManagementConstant.USER_CONST_START);
		List<UserListResponse> userDTOs = projectService.getAllUsers();
		log.info(ProjectManagementConstant.USER_CONST_END);
		return new ResponseEntity<List<UserListResponse>>(userDTOs, HttpStatus.OK);
	}

	@GetMapping(ProjectManagementConstant.GET_PROJECT_BY_ID)
	@Operation(summary = ProjectManagementConstant.GET_PROJECT_BY_ID_SUMMARY, description = ProjectManagementConstant.GET_PROJECT_BY_ID_DESCRIPTION)
	public ResponseEntity<?> getProjectById(@PathVariable String id) {
		log.info("ProjectManagementController, getProjectById starts with the id {}",id);
		Project proj = projectService.getProjectById(id);
		if (proj != null) {
			log.info("ProjectManagementController, getProjectById ends with the id {}",id);
			return new ResponseEntity<Project>(proj, HttpStatus.OK);
		} else
			log.error("ProjectManagementController, getProjectById id not found");
		return new ResponseEntity<String>(ProjectManagementConstant.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);

	}

	@GetMapping(ProjectManagementConstant.VIEW_PROJECTS_BY_OWNER)
	@Operation(summary = ProjectManagementConstant.GET_PROJECTS_SUMMARY, description = ProjectManagementConstant.GET_PROJECTS_DESCRIPTION)
	public ResponseEntity<?> searchProjectDataByBrand(@PathVariable String createdBy) {
		log.info("ProjectManagementController searchProjectsByOwner starts with {}",createdBy);
		List<Project> projects = projectService.searchByCreatedBy(createdBy);
		if (projects.isEmpty()) {
			return new ResponseEntity<String>(ProjectManagementConstant.PROJECTS_NOT_FOUND, HttpStatus.NOT_FOUND);
		} else
			log.info("ProjectManagementController searchProjectsByOwner ends");
		return new ResponseEntity<List<Project>>(projects, HttpStatus.FOUND);
	}

	@GetMapping(ProjectManagementConstant.VIEW_ALL_PROJECTS)
	@Operation(summary = ProjectManagementConstant.GET_ALL_PROJECTS_SUMMARY, description = ProjectManagementConstant.GET_ALL_PROJECTS_DESCRIPTION)
	public ResponseEntity<?> viewAllProjects() {
		log.info("ProjectManagementController ViewAllProjects starts");
		List<Project> projects = projectService.getAllProjects();

		if (projects.isEmpty()) {
			log.info("ProjectManagementController ViewAllProjects No projects found");
			return new ResponseEntity<String>("PROJECTS_NOT_FOUND", HttpStatus.NOT_FOUND);
		} else {
			log.info("ProjectManagementController ViewAllProjects ends");
			return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
		}
	}

	@PutMapping(ProjectManagementConstant.EDIT_PROJECT)
	@Operation(summary = ProjectManagementConstant.EDIT_PROJECT_SUMMARY, description = ProjectManagementConstant.EDIT_PROJECT_DESCRIPTION)
	public ResponseEntity<?> updateProjectDetails(@Valid @RequestBody ProjectRequest projectRequest) {
		log.info("ProjectManagementController EditProjects starts {}",projectRequest);
		ProjectRequest edit = projectService.editProject(projectRequest);
		log.info("ProjectManagementController EditProjects ends {}",edit);
		return new ResponseEntity<>(edit, HttpStatus.ACCEPTED);
	}
}