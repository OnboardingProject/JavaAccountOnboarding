package com.account.onboarding.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.entity.Project;
import com.account.onboarding.entity.ProjectTaskDetails;
import com.account.onboarding.entity.TaskDetails;
import com.account.onboarding.entity.Types;
import com.account.onboarding.entity.UserEntity;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.request.SaveTaskStatusRequest;
import com.account.onboarding.request.TaskStatusRequest;
import com.account.onboarding.response.ProjectDetailsResponse;
import com.account.onboarding.response.UserDetailsResponse;
import com.account.onboarding.util.ProjectOnboardingUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Amrutha Joseph
 * @description Service class for project onboarding
 * @created_Date 17/08/2022
 */

@Slf4j
@Service
public class ProjectOnboardingService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ProjectOnboardingUtil projectOnboardingUtil;

	/**
	 * @param userId
	 * @return ProjectDetails object
	 * @throws ProjectOnboardingException
	 * @description Fetch all the projects assigned to the particular user
	 */

	public List<ProjectDetailsResponse> getProjectsBasedOnUser(String userId) //throws Exception
	{

		log.info("ProjectOnboarding Service get Projects based on user starts with userId {}",userId);

		Criteria criteria = Criteria.where("userId").is(userId);
		Query query = projectOnboardingUtil.createQuery(criteria);

		UserEntity user = mongoTemplate.findOne(query, UserEntity.class);

		if (user != null) {

			log.info("ProjectOnboarding Service get Projects based on user, User found");

			List<ProjectDetailsResponse> projectList = user.getProjectTasks().stream()
					.map(pId -> getProjectNames(pId.getProjectId())).collect(Collectors.toList());

			log.info("ProjectOnboarding Service get Projects based on user, Project list returned");
			return projectList;

		} else {

			log.warn("ProjectOnboarding Service get Projects based on user,no user found with given id");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.USER_NOT_FOUND);
		}

	}

	/**
	 * @param projectId
	 * @return ProjectDetailsResponse object
	 * @throws ProjectOnboardingException
	 * @description Fetch project names by using project id.
	 */

	public ProjectDetailsResponse getProjectNames(String projectId) {

		log.info("ProjectOnboarding Service getProjectNames method starts");

		ProjectDetailsResponse projectDetails = new ProjectDetailsResponse();

		Criteria criteria = Criteria.where(ProjectOnboardingConstant.PROJECT_ID).is(projectId);
		Query query = projectOnboardingUtil.createQuery(criteria);

		Project project = mongoTemplate.findOne(query, Project.class);

		if (project != null) {

			log.info("ProjectOnboarding Service getProjectNames, found and setting the project details");

			projectDetails.setProjectId(project.getProjectId());
			projectDetails.setProjectName(project.getProjectName());

			return projectDetails;
		} else {

			log.warn("ProjectOnboarding Service getProjectNames, No Project found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);

		}

	}

	/**
	 * @param projectId
	 * @return UserDetails object
	 * @throws ProjectOnboardingException
	 * @description Fetch all the resources assigned to the particular project
	 */

	public List<UserDetailsResponse> getUsersBasedOnProject(String projectId) //throws Exception
	{

		log.info("ProjectOnboardingService the get user list starts with projectId {}", projectId);

		Criteria criteria = Criteria.where(ProjectOnboardingConstant.PROJECT_ID).is(projectId);
		Query query = projectOnboardingUtil.createQuery(criteria);

		Project project = mongoTemplate.findOne(query, Project.class);

		if (project != null) {

			log.info("ProjectOnboardingService the get user list, Project found");

			List<UserDetailsResponse> resources = project.getUserIds().stream().map(this::getResource)
					.filter(Objects::nonNull).collect(Collectors.toList());

			log.info("ProjectOnboardingService resource list returned");
			return resources;

		} else {

			log.warn("ProjectOnboardingService, No Project found with given id");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}

	}

	/**
	 * @param userId
	 * @return UserDetailsResponse object
	 * @description Check whether the given user is a resource or not.
	 */

	public UserDetailsResponse getResource(String userId) {

		log.info("ProjectOnboarding Service, the getResource method starts with userId {}",userId);

		UserDetailsResponse userDetails = new UserDetailsResponse();

		Types type = mongoTemplate.findOne(
				Query.query(new Criteria().andOperator(Criteria.where(ProjectOnboardingConstant.TYPE_NAME).is(ProjectOnboardingConstant.ROLE),
						Criteria.where(ProjectOnboardingConstant.TYPE_DESC).is(ProjectOnboardingConstant.RESOURCE))),
				Types.class);

		Criteria criteria = new Criteria().andOperator(Criteria.where(ProjectOnboardingConstant.USER_ID).is(userId),
				Criteria.where(ProjectOnboardingConstant.ROLE_ID).is(type.getTypeId()));

		Query query = projectOnboardingUtil.createQuery(criteria);

		UserEntity user = mongoTemplate.findOne(query, UserEntity.class);

		if (user != null) {

			log.info("ProjectOnboarding Service, the getResource method the user details");

			userDetails.setUserId(user.getUserId());
			userDetails.setUserName(user.getFirstName() + " " + user.getLastName());

			return userDetails;
		} else {

			log.info("ProjectOnboarding Service, the getResource method, Given user is not a resource");
			return null;
		}
	}

	/**
	 * @param projectId, resourceId
	 * @return Task List object
	 * @throws ProjectOnboardingException
	 * @description : Show Task List associated with Project and Resource
	 */

	public List<TaskDetails> fetchTaskList(String projectId, String resourceId) //throws Exception 
	{
		log.info("ProjectOnboardingService fetch task list starts with projectId {}",projectId);
		Query query = projectOnboardingUtil.createQuery(new Criteria().andOperator(
				Criteria.where(ProjectOnboardingConstant.USER_ID).is(resourceId), Criteria.where(ProjectOnboardingConstant.PROJECT_TASKS_PROJ_ID).is(projectId)));

		List<UserEntity> users = mongoTemplate.find(query, UserEntity.class);
		if (!CollectionUtils.isEmpty(users)) {
			List<ProjectTaskDetails> projectIds1 = users.get(0).getProjectTasks();
			List<TaskDetails> tasks = projectIds1.get(0).getTasks();
			log.info("ProjectOnboardingService fetch task list ends");
			return tasks;
		} else {
			log.error("ProjectOnboardingService fetch task list ends with exception");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_LIST_NOT_FOUND);
		}
	}

	/**
	 * @param projectId, userId, taskId, taskStatus
	 * @return Task List object
	 * @throws ProjectOnboardingException
	 * @description : Save Task Status based on User and project Tasks.
	 */

	public List<TaskDetails> saveStatus(SaveTaskStatusRequest saveTaskStatusRequest) //throws Exception
	{
		log.info("Method for saving the task status");
		Query query = projectOnboardingUtil
				.createQuery(new Criteria().andOperator(Criteria.where(ProjectOnboardingConstant.USER_ID).is(saveTaskStatusRequest.getUserId()),
						Criteria.where(ProjectOnboardingConstant.PROJECT_TASKS_PROJ_ID).is(saveTaskStatusRequest.getProjectId())));

		List<UserEntity> users = mongoTemplate.find(query, UserEntity.class);

		if (!CollectionUtils.isEmpty(users)) {
			List<ProjectTaskDetails> projectIds1 = users.get(0).getProjectTasks();
			List<TaskDetails> tasks = projectIds1.get(0).getTasks();
			for (TaskStatusRequest taskStatusRequest : saveTaskStatusRequest.getTaskStatusRequest()) {

				List<TaskDetails> taskWithGivenTaskId = tasks.stream()
						.filter(s -> s.getTaskId() == taskStatusRequest.getTaskId()).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(taskWithGivenTaskId)) {
					taskWithGivenTaskId.get(0).setTaskStatus(taskStatusRequest.getTaskStatus());
					Update update = new Update();
					update.set(ProjectOnboardingConstant.PROJECT_TASKS, projectIds1);
					mongoTemplate.updateFirst(query, update, UserEntity.class);
				} else {
					log.error("ProjectOnboardingService saveStatus method Task not found");
					throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND);
				}
			}
			return tasks;
		} else {
			log.error("ProjectOnboardingService saveStatus method ProjectId not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}

	}

	/**
	 * @param
	 * @return List of Types object
	 * @throws ProjectOnboardingException
	 * @description Fetch all task status
	 */
	public List<Types> getAllTaskStatus()// throws Exception
	{
		log.info("ProjectOnboarding Service getAllTasks starts");
		Query query = projectOnboardingUtil
				.createQuery(Criteria.where(ProjectOnboardingConstant.TYPE_NAME).is(ProjectOnboardingConstant.TASK_STATUS));
		List<Types> taskStatusList = mongoTemplate.find(query, Types.class);

		if (!CollectionUtils.isEmpty(taskStatusList)) {
			log.info("ProjectOnboarding Service getAllTasks display all the task status");
			return taskStatusList;
		} else {
			log.error("ProjectOnboarding Service getAllTasks the task list is empty");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.STATUS_LIST_EMPTY);
		}
	}
}