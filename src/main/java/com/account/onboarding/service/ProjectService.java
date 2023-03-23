package com.account.onboarding.service;

import java.util.List;

import com.account.onboarding.entity.Project;
import com.account.onboarding.request.ProjectRequest;
import com.account.onboarding.response.UserListResponse;

/**
 * Interface for project Management Service methods
 * 
 *
 */
public interface ProjectService {

	ProjectRequest createProject(ProjectRequest projectDTO);

	List<UserListResponse> getAllUsers();

	Project getProjectById(String id);

	List<Project> getAllProjects();

	List<Project> searchByCreatedBy(String createdBy);

	ProjectRequest editProject(ProjectRequest projectVO);

}
