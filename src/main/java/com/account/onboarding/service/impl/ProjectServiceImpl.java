package com.account.onboarding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.account.onboarding.constants.ProjectManagementConstant;
import com.account.onboarding.entity.DatabaseSequence;
import com.account.onboarding.entity.Project;
import com.account.onboarding.entity.ProjectTaskDetails;
import com.account.onboarding.entity.TaskDetails;
import com.account.onboarding.entity.UserEntity;
import com.account.onboarding.exception.DataNotFoundException;
import com.account.onboarding.exception.NameAlreadyExistingException;
import com.account.onboarding.exception.NoResourceFoundException;
import com.account.onboarding.repository.ProjectRepository;
import com.account.onboarding.request.ProjectRequest;
import com.account.onboarding.response.UserListResponse;
import com.account.onboarding.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class contains the Project Management service methods which interacts
 * with the Repository
 */

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * This method will convert the POJO class into project entity class and persist
	 * it into DB and responses from the repository is converted back to the
	 * controller
	 * 
	 * @throws NameAlreadyExistingException
	 */
	
	@Override
	public ProjectRequest createProject(ProjectRequest projectDTO) {
		log.info(ProjectManagementConstant.PJT_CONST_SERVICE);
		String incrementedProjectSeq = ProjectManagementConstant.PROJECT_ID_STARTS
				+ generateProjectSequence(ProjectManagementConstant.PROJECT_SEQUENCE_ID);
		if (projectRepository.findByProjectName(projectDTO.getProjectName()) != null) {
			log.error(ProjectManagementConstant.PJT_CONST_SERVICE_EXCN_LOG, projectDTO.getProjectName());
			throw new NameAlreadyExistingException(ProjectManagementConstant.PJT_CONST_SERVICE_EXCN);
		}
		Date time = new Date();

		Project pjt = projectRepository.save(new Project(incrementedProjectSeq, projectDTO.getProjectName(),
				projectDTO.getProjectDescription(), time, projectDTO.getCreatedBy(), projectDTO.getLastUpdateBy(), time,
				projectDTO.getUserId(), null));
		for (String userid : projectDTO.getUserId()) {

			Query query = new Query();
			query.addCriteria(Criteria.where(ProjectManagementConstant.USER_ID).is(userid));
			UserEntity user = mongoTemplate.findOne(query, UserEntity.class);
			if (ObjectUtils.isNotEmpty(user)) {

				ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();

				List<ProjectTaskDetails> projectTaskListExist = new ArrayList<>();
				if (ObjectUtils.isNotEmpty(user.getProjectTasks())) {
					projectTaskListExist = user.getProjectTasks();

				}
				projectTaskDetails.setProjectId(incrementedProjectSeq);
				List<TaskDetails> userTaskList = new ArrayList<TaskDetails>();
				projectTaskDetails.setTasks(userTaskList);

				projectTaskListExist.add(projectTaskDetails);

				user.setProjectTasks(projectTaskListExist);

				Update userUpdate = new Update();
				userUpdate.set(ProjectManagementConstant.PROJECT_TASKS, user.getProjectTasks());
				mongoTemplate.upsert(query, userUpdate, UserEntity.class);
			}
		}
		log.info(ProjectManagementConstant.PJT_CONST_SERVICE_COMPLETE);
		projectDTO.setProjectId(pjt.getProjectId());
		projectDTO.setCreatedTime(time);
		projectDTO.setLastUpdateTime(time);
		return projectDTO;

	}

	/**
	 * This method will get all resources from repo who has Resource roles
	 */
	@Override
	public List<UserListResponse> getAllUsers() {
		log.info(ProjectManagementConstant.USER_CONST_SERVICE);
		List<UserEntity> users = mongoTemplate.findAll(UserEntity.class);
		if (users == null || users.isEmpty()) {
			log.error(ProjectManagementConstant.USER_CONST_SERVICE_EXCN_LOG);
			throw new NoResourceFoundException(ProjectManagementConstant.USER_CONST_SERVICE_EXCN);
		}
		List<UserListResponse> userDTOs = users.stream()
				.map((s) -> new UserListResponse(s.getUserId(), s.getUserName(), s.getRoleId()))
				.collect(Collectors.toList());
		log.info(ProjectManagementConstant.USER_CONST_SERVICE_COMPLETE);
		return userDTOs;
	}

	/**
	 * This method will return the incremented long value of the Project sequence
	 * 
	 * @param seqName
	 * @return
	 */
	private long generateProjectSequence(String seqName) {
		DatabaseSequence counter = mongoTemplate.findAndModify(
				Query.query(Criteria.where(ProjectManagementConstant.ID).is(seqName)),
				new Update().inc(ProjectManagementConstant.SEQUENCE, 1),
				FindAndModifyOptions.options().returnNew(true).upsert(true), DatabaseSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

	@Override
	public ProjectRequest editProject(ProjectRequest projectDTO) {

		Optional<Project> opt = projectRepository.findById(projectDTO.getProjectId());

		if (opt.isPresent()) {
			log.info("Edit project is started");
			Project getProject = projectRepository.findById(projectDTO.getProjectId()).get();
			getProject.setProjectName(projectDTO.getProjectName());
			getProject.setProjectDescription(projectDTO.getProjectDescription());
			getProject.setLastUpdateBy(projectDTO.getLastUpdateBy());
			Date date = new Date();
			getProject.setLastUpdateTime(date);
			getProject.setUserIds(projectDTO.getUserId());

			Project edit = projectRepository.save(getProject);

			projectDTO.setProjectId(edit.getProjectId());
			projectDTO.setCreatedBy(getProject.getCreatedBy());
			projectDTO.setCreatedTime(getProject.getCreatedTime());
			projectDTO.setLastUpdateTime(date);

			for (String userid : projectDTO.getUserId()) {

				Query query = new Query();
				query.addCriteria(Criteria.where(ProjectManagementConstant.USER_ID).is(userid));
				UserEntity user = mongoTemplate.findOne(query, UserEntity.class);
				if (ObjectUtils.isNotEmpty(user)) {

					ProjectTaskDetails projectTaskDetails = new ProjectTaskDetails();

					List<ProjectTaskDetails> projectTaskListExist = new ArrayList<>();
					if (ObjectUtils.isNotEmpty(user.getProjectTasks())) {
						projectTaskListExist = user.getProjectTasks();

					}
					if (projectTaskListExist.stream().filter(a -> a.getProjectId().equals(projectDTO.getProjectId()))
							.count() == 0) {
						projectTaskDetails.setProjectId(edit.getProjectId());
						List<TaskDetails> userTaskList = new ArrayList<TaskDetails>();

						projectTaskDetails.setTasks(userTaskList);
						projectTaskListExist.add(projectTaskDetails);
						user.setProjectTasks(projectTaskListExist);
						Update userUpdate = new Update();
						userUpdate.set(ProjectManagementConstant.PROJECT_TASKS, user.getProjectTasks());
						mongoTemplate.upsert(query, userUpdate, UserEntity.class);

					}

				}
			}

			log.info("Project updated successfully");
			return projectDTO;
		} else {
			throw new DataNotFoundException(ProjectManagementConstant.NO_DATA_FOUND);
		}
	}

	@Override
	public Project getProjectById(String id) {
		log.info("ProjectService getProjectById starts. ");
		Optional<Project> proj = projectRepository.findById(id);
		if (proj.isPresent()) {
			log.info("In Service class finding  employee by id");
			return proj.get();
		} else
			throw new DataNotFoundException(ProjectManagementConstant.NO_PROJECTS_WITH_GIVEN_ID);
	}

	@Override
	public List<Project> searchByCreatedBy(String createdBy) {
		log.info("ProjectService getProjectByOwner starts");

		List<Project> projects = projectRepository.findByCreatedBy(createdBy);

		if (projects.isEmpty()) {
			log.error("Invalid data");
			throw new DataNotFoundException(ProjectManagementConstant.NO_PROJECTS_TO_DISPLAY);
		} else {
			return projects;
		}
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> ProjectList = projectRepository.findAll();

		if (ProjectList.isEmpty()) {
			throw new DataNotFoundException(ProjectManagementConstant.NO_PROJECTS_TO_DISPLAY);
		} else
			return ProjectList;
	}

}