package com.account.onboarding.controller;

import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.account.onboarding.entity.Project;
import com.account.onboarding.request.ProjectRequest;
import com.account.onboarding.response.UserListResponse;
import com.account.onboarding.service.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectManagementControllerTest {
	@InjectMocks
	private ProjectManagementController projectManagementController;

	@Mock
	private ProjectService projectService;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(projectManagementController).build();
	}

	private List<Project> projectList = new ArrayList<>();
	private List<String> userId1 = new LinkedList<String>(Arrays.asList("u12", "u15", "u45"));
	private List<String> userId2 = new LinkedList<String>(Arrays.asList("u44", "u14", "u43"));
	private List<String> userId3 = new LinkedList<String>(Arrays.asList("u17", "u25", "u95"));
	Date time = new Date();

	private ProjectRequest getProjectTestData() {
		List<String> userId = new LinkedList<String>();
		userId.add("U23");
		userId.add("U36");
		ProjectRequest projectDTO = new ProjectRequest("Pjt111", "PJT_ONBOARDING", "About on boarding", null, "U111", null,
				"U111", userId);

		return projectDTO;
	}

	private List<UserListResponse> getUserTestData() {
		List<UserListResponse> users = new ArrayList<UserListResponse>();
		UserListResponse user = new UserListResponse("u111", "JEENA", 4);
		UserListResponse user1 = new UserListResponse("u112", "Mareena", 4);
		users.add(user1);
		users.add(user);
		return users;
	}

	@Test
	public void addProjectTest() throws ParseException, Exception {
		ProjectRequest pjtDTO = getProjectTestData();
		when(projectService.createProject(Mockito.any(ProjectRequest.class))).thenReturn(pjtDTO);
		mockMvc.perform(MockMvcRequestBuilders.post("/project/add-project")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(pjtDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	void whenRequestParameterIsInvalid_thenReturnsStatus400Test() throws Exception {
		ProjectRequest projectDTO1 = new ProjectRequest(" ", "PJT_ONBOARDING", "About on boarding", null, "U111", null, "U111",
				null);
		mockMvc.perform(MockMvcRequestBuilders.post("/project/add-project")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(projectDTO1)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	public void getAllResourceTest() throws ParseException, Exception {
		when(projectService.getAllUsers()).thenReturn(getUserTestData());
		mockMvc.perform(MockMvcRequestBuilders.get("/project/get-resources").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void updateProjectTest() throws Exception {
		ProjectRequest projectDTO = getProjectTestData();
		List<String> userlist = Arrays.asList("u12", "u34");
		projectDTO.setProjectName("ATM Project");
		projectDTO.setProjectDescription("Description of the project");
		projectDTO.setLastUpdateBy("U112");
		projectDTO.setUserId(userlist);
		projectDTO.setLastUpdateTime(null);

		when(projectService.editProject(Mockito.any(ProjectRequest.class))).thenReturn(projectDTO);
		mockMvc.perform(MockMvcRequestBuilders.put("/project/edit-project")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(projectDTO)))
				.andExpect(MockMvcResultMatchers.status().isAccepted());
	}

	@Test
	public void viewAllProjectsSuccessTest() throws JsonProcessingException, Exception {
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);
		Project project2 = new Project("62f47fb13ff026663334d221", "starter", "stringstri", time, "Nishanti", "Jeena",
				time, userId2, null);
		Project project3 = new Project("62f47fb13ff026663334d222", "social", "stringstri", time, "Jeena", "Mareena",
				time, userId3, null);
		projectList.add(project1);
		projectList.add(project2);
		projectList.add(project3);

		when(projectService.getAllProjects()).thenReturn(projectList);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/project/view-all-projects").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void viewAllProjectsFailureTest() throws JsonProcessingException, Exception {
		projectList.clear();

		when(projectService.getAllProjects()).thenReturn(projectList);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/project/view-all-projects").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void searchByOwnerTest() throws JsonProcessingException, Exception {
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);
		Project project2 = new Project("62f47fb13ff026663334d221", "starter", "stringstri", time, "Nishanti", "Jeena",
				time, userId2, null);
		Project project3 = new Project("62f47fb13ff026663334d222", "social", "stringstri", time, "Jeena", "Mareena",
				time, userId3, null);
		projectList.add(project1);
		projectList.add(project2);
		projectList.add(project3);

		when(projectService.searchByCreatedBy("Mareena")).thenReturn(projectList);

		mockMvc.perform(MockMvcRequestBuilders.get("/project/view-projects-by-owner/Mareena")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isFound());
	}

	@Test
	public void searchByOwnerTestFailureTest() throws JsonProcessingException, Exception {
		projectList.clear();
		when(projectService.searchByCreatedBy("Mareena")).thenReturn(projectList);

		mockMvc.perform(MockMvcRequestBuilders.get("/project/view-projects-by-owner/Mareena")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void getProjectByIdTest() throws Exception {
		Project project1 = new Project("62f47fb13ff026663334d220", "onboarding", "stringstri", time, "Mareena",
				"Nishanti", time, userId1, null);

		when(projectService.getProjectById(Mockito.anyString())).thenReturn(project1);
		mockMvc.perform(MockMvcRequestBuilders.get("/project/view-project/62f47fb13ff026663334d220")
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void getProjectByIdTestFailure() throws Exception {

		when(projectService.getProjectById(Mockito.anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get("/project/view-project/62f47fb13ff026663334d229")
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}
}