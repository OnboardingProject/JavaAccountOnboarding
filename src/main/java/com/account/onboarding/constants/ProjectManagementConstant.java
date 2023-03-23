package com.account.onboarding.constants;

/**
 * Class which declared constants associated with Project Management
 *
 */
public interface ProjectManagementConstant {
	
	public static final String PJT_CONST_START = "In controller create method started";
	public static final String PJT_CONST_END = "In controller create method created project";
	public static final String PJT_CONST_SERVICE = "In Service create method started";
	public static final String PJT_CONST_SERVICE_COMPLETE = "In Service create method completed";
	public static final String USER_CONST_START = "In controller get all resource method started";
	public static final String USER_CONST_END = "In controller got all resources";
	public static final String USER_CONST_SERVICE = "In Service get all user by role started";
	public static final String USER_CONST_SERVICE_COMPLETE = "In Service get all user by role completed";
	public static final String USER_CONST_SERVICE_EXCN_LOG = "No Users found in the DB ";
	public static final String USER_CONST_SERVICE_EXCN = "No Users found";
	public static final String PJT_CONST_SERVICE_EXCN_LOG = "A project already exists with name {}";
	public static final String PJT_CONST_SERVICE_EXCN = "project name already existing";
	public static final String PROJECT="project";
	public static final String ADD_PROJECT="/add-project";
	public static final String ADD_PROJECT_SUMMARY="Add a new Project";
	public static final String ADD_PROJECT_DESCRIPTION="This API is used to add a new project";
	public static final String GET_RESOURCES="/get-resources";
	public static final String GET_USERS_SUMMARY="Get all users";
	public static final String GET_USERS_DESCRIPTION="This API is used to get all users";
	public static final String GET_PROJECT_BY_ID="/view-project/{id}";
	public static final String GET_PROJECT_BY_ID_SUMMARY="Get project by id";
	public static final String GET_PROJECT_BY_ID_DESCRIPTION="This API is used to get a project by id";
	public static final String PROJECT_NOT_FOUND="PROJECT_NOT_FOUND";
	public static final String VIEW_PROJECTS_BY_OWNER="/view-projects-by-owner/{createdBy}";
	public static final String GET_PROJECTS_SUMMARY="Get projects by an owner";
	public static final String GET_PROJECTS_DESCRIPTION="This API is used to get all projects created by a user";
	public static final String PROJECTS_NOT_FOUND="PROJECTS_NOT_FOUND";
	public static final String VIEW_ALL_PROJECTS="/view-all-projects";
	public static final String GET_ALL_PROJECTS_SUMMARY="Get all projects";
	public static final String GET_ALL_PROJECTS_DESCRIPTION ="This API is used to get all projects";
	public static final String EDIT_PROJECT="/edit-project";
	public static final String EDIT_PROJECT_SUMMARY="Update a project details";
	public static final String EDIT_PROJECT_DESCRIPTION="This API is used to update a project details";
	public static final String PROJECT_ID_STARTS="PROJ_";
	public static final String PROJECT_SEQUENCE_ID="project-sequence-id";
	public static final String USER_ID= "userId";
	public static final String PROJECT_TASKS="projectTasks";
	public static final String ID="_id";
	public static final String SEQUENCE="seq";
	public static final String NO_PROJECTS_TO_DISPLAY="No projects to display";
	public static final String NO_PROJECTS_WITH_GIVEN_ID="No projects with given Id";
	public static final String NO_DATA_FOUND="No data is found";
	
}