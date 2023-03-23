package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.UserManagementConstant;
import com.account.onboarding.entity.UserEntity;
import com.account.onboarding.exception.UserNotFoundException;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;
import com.account.onboarding.response.ErrorTextResponse;
import com.account.onboarding.response.UserListResponse;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.IUserService;
import com.account.onboarding.util.UserValidation;
import com.mongodb.MongoException;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the controller class of UserManagement where we add, update and view the
 * details of User
 * 
 *
 */
@Slf4j
@RestController
@RequestMapping(UserManagementConstant.USER)
public class UserController {
	
	@Autowired
	private IUserService userService;

	@Autowired
	private UserValidation validation;
	
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * @param userRequest
	 * @return user entity
	 */
	@PostMapping(UserManagementConstant.ADD_USER)
	@Operation(summary = UserManagementConstant.ADD_USER_SUMMARY, description = UserManagementConstant.ADD_USER_DESCRIPTION)
	public ResponseEntity<UserEntity> saveUser(@RequestBody UserRequest userRequest) {

		log.info("Controller saveUser starts with request: {} ", userRequest);
		validation.validateUser(userRequest);
		validation.phoneValidation(userRequest);
		validation.emailValidation(userRequest);
		UserEntity userAdd = userService.addUser(userRequest);
		log.info("Controller saveUser ends with httpstatus CREATED with response: {}", userAdd);
		return new ResponseEntity<>(userAdd, HttpStatus.CREATED);
	}

	/**
	 * @param userEdit
	 * @return user entity
	 */
	@PutMapping(UserManagementConstant.EDIT_USER)
	@Operation(summary = UserManagementConstant.EDIT_USER_SUMMARY, description = UserManagementConstant.EDIT_USER_DESCRIPTION)
	public ResponseEntity<UserEntity> updateUser(@RequestBody UserEditRequest userEdit) {

		log.info("Controller updateUser starts with request: {} ", userEdit);
		validation.updateUserValidation(userEdit);
		UserEntity userUpdate = userService.updateUser(userEdit);
		log.info("Controller updateUser method ends with statuscode OK with response: {}", userUpdate);
		return new ResponseEntity<>(userUpdate, HttpStatus.OK);
	}

	/**
	 * Description: view all users will display the list of all users based on
	 * hierarchy
	 * 
	 * @param userId
	 * @return responseUserList
	 * @throws UserNotFoundException
	 * @throws DBException
	 */

	@GetMapping(UserManagementConstant.GET_USERS_BY_ID)
	@Operation(summary = UserManagementConstant.GET_USERS_BY_ID_SUMMARY, description = UserManagementConstant.GET_USERS_BY_ID_DESCRIPTION)
	public ResponseEntity<?> viewAllUserByHeirarchy(@PathVariable String userId)
			throws UserNotFoundException, MongoException {
		log.info(" Contoller viewAllUserByHierarchy starts with userId : {} " + userId);
		List<UserListResponse> responseUserList = userService.viewAllUserByHeirarchy(userId);
		if (!CollectionUtils.isEmpty(responseUserList)) {
			log.info("Controller viewAllUserByHierarachy method ends with http status OK");
			return new ResponseEntity<>(responseUserList, HttpStatus.OK);
		} else {
			log.error("Controller viewAllUserByHierarchy method ends with http status NO_CONTENT");
			return new ResponseEntity<>(responseUserList, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Description : Search a user will return the requested user based on user
	 * firstName
	 * 
	 * @param firstName
	 * @return users
	 * @throws UserNotFoundException
	 * @throws DBException
	 */

	@GetMapping(UserManagementConstant.GET_USERS_BY_NAME)
	@Operation(summary = UserManagementConstant.GET_USERS_BY_NAME_SUMMARY, description = UserManagementConstant.GET_USERS_BY_NAME_DESRIPTION)
	public ResponseEntity<?> findByFirstName(@PathVariable(UserManagementConstant.FIRST_NAME) String firstName)
			throws UserNotFoundException, MongoException {
		log.info("Controller findByUserFirstName method starts with firstName : {}" + firstName);
		List<UserListResponse> users = userService.findByFirstName(firstName);
		if (!CollectionUtils.isEmpty(users)) {
			log.info("Controller findByUserFirstName method ends with http status OK");
			return new ResponseEntity<>(users, HttpStatus.OK);
		} else {
			log.error("Controller findByUserFirstName method ends with http status NO_CONTENT");
			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
		}

	}
	
	/**
	 * Description: view user by userId
	 * 
	 * @param userId
	 * @return user
	 * @throws UserNotFoundException
	 * @throws DBException
	 */

	@GetMapping(UserManagementConstant.USER_ID_URL)
	@Operation(summary = UserManagementConstant.GET_USERS_BY_ID_SUMMARY, description = UserManagementConstant.GET_USERS_BY_ID_DESCRIPTION)
	public ResponseEntity<?> viewUser(@PathVariable String userId)
			 {
		log.info(" UserContoller viewUserByUserId starts with userId : {} " , userId);
		UserResponse responseUserList = userService.viewUser(userId);
			log.info("UserContoller viewUserByUserId method ends with http status OK");
			return new ResponseEntity<>(responseUserList, HttpStatus.OK);
	}

	/**
	 * Method to delete userIds
	 * @param userIds
	 * @return
	 */
	@DeleteMapping(UserManagementConstant.DELETE_USER)
	@Operation(summary = UserManagementConstant.DELETE_USER_SUMMARY, description = UserManagementConstant.DELETE_USER_DESCRIPTION)
	public ResponseEntity<?> deleteUser(@RequestBody List<String> userIds) {

		ResponseEntity<?> responseEntity = null;
		ArrayList<ErrorTextResponse> errorText = userService.deleteUser(userIds);
		if (errorText.isEmpty()) {
			responseEntity = new ResponseEntity<>(UserManagementConstant.DELETE_USER_RESPONSE, HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<>(errorText, HttpStatus.OK);
		}
		return responseEntity;
	}
}
