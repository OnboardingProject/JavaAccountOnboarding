package com.account.onboarding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.account.onboarding.constants.AccountOverviewConstant;
import com.account.onboarding.constants.UserManagementConstant;
import com.account.onboarding.dao.impl.UserDaoImpl;
import com.account.onboarding.entity.Account;
import com.account.onboarding.entity.AccountDocument;
import com.account.onboarding.entity.TaskDetails;
import com.account.onboarding.entity.UserEntity;
import com.account.onboarding.exception.AccountCustomerException;
import com.account.onboarding.exception.RoleAlreadySameException;
import com.account.onboarding.exception.RoleIdNullException;
import com.account.onboarding.exception.UserAlreadyFoundException;
import com.account.onboarding.exception.UserNotFoundException;
import com.account.onboarding.exception.UserUpdateException;
import com.account.onboarding.repository.TypesRepository;
import com.account.onboarding.repository.UserRepository;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;
import com.account.onboarding.response.AccountResponse;
import com.account.onboarding.response.ErrorTextResponse;
import com.account.onboarding.response.UserListResponse;
import com.account.onboarding.response.UserResponse;
import com.account.onboarding.service.IUserService;
import com.mongodb.MongoException;

import lombok.extern.slf4j.Slf4j;

/**
 * This is the service class for UserManagement where we write business logic
 * for add user and update user
 * 
 * @author
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TypesRepository typesRepository;

	@Autowired
	private UserDaoImpl userDaoImpl;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * This is the add method of UserManagement
	 * 
	 * @param userRequest
	 * @return user
	 * 
	 */
	@Override
	public UserEntity addUser(UserRequest userRequest) {
		log.info("Started service class of add user");
		UserEntity user = new UserEntity();
		Optional<UserEntity> existUser = userRepository.findById(userRequest.getUserId());

		if (!existUser.isPresent()) {
			String encodedPassWord = passwordEncoder.encode(userRequest.getPassword());
			user.setUserId(userRequest.getUserId());
			user.setUserName(userRequest.getUserName());
			user.setPassword(encodedPassWord);
			user.setAccountId(userRequest.getAccountId());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setEmailId(userRequest.getEmailId());
			user.setPhoneNo(userRequest.getPhoneNumber());
			user.setPortfolio(userRequest.getPortfolio());
			user.setDesignation(userRequest.getDesignation());
			user.setCreatedTime(new Date());
			user.setCreatedBy(userRequest.getCreatedBy());
			user.setLastUpdateBy(userRequest.getLastUpdatedBy());
			user.setLastUpdateTime(new Date());
			user.setRoleId(userRequest.getRoleId());
			TaskDetails taskDetails = new TaskDetails();
			List<TaskDetails> taskList= new ArrayList<>();
			AccountDocument accountDocument= new AccountDocument();
			List<AccountDocument> documentList= new ArrayList<>();
			user.setAccountDocuments(documentList);
			user.setAccountTasks(taskList);
			String hierarchys = userDaoImpl.getUser(userRequest.getAccountId(), user.getCreatedBy());
			if (StringUtils.isNotEmpty(hierarchys)) {
				user.setHierarchy(userRequest.getUserId().concat(", ").concat(hierarchys));
			} else {
				user.setHierarchy(userRequest.getUserId());
			}
			log.info("Service addUser user has been saved");
			userRepository.save(user);
			if (ObjectUtils.isEmpty(user)) {
				log.error("UserService addUser user is not saved in database");
				throw new MongoException("user is not saved in database");
			}
			return user;

		}

		else {
			log.error("UserService addUser existing user");
			throw new UserAlreadyFoundException("user already exists");
		}
	}

	/**
	 *
	 * This is the update method of UserManagement
	 * 
	 * @param userEdit
	 * @return user
	 * @throws RoleIdNullException      if entered RoleId is invalid
	 * @throws RoleAlreadySameException if entered RoleId is same of that particular
	 *                                  user
	 * @throws MongoException           if user updating has not done in database
	 * @throws UserNotFoundException    if user does not exists
	 * @throws UserUpdateException      if user update does not work at any other
	 *                                  chance
	 */
	@Override
	public UserEntity updateUser(UserEditRequest userEdit) {
		log.info("Userservice update user starts with request: {}", userEdit);

		Optional<UserEntity> existingUser = userRepository.findById(userEdit.getUserId());

		log.info("user fetched from db {}", existingUser);
		UserEntity oldUser = null;
		if (existingUser.isPresent()) {

			oldUser = existingUser.get();

			if (oldUser.getRoleId().equals(userEdit.getRoleId())) {
				log.error("Service updateUser same roleId has been given for the user");
				throw new RoleAlreadySameException(UserManagementConstant.ROLE_SAME_WITH_USER + oldUser.getUserName());

			} else {
				Integer roleid = userDaoImpl.getRoleId(userEdit.getRoleId());
				if (roleid != null) {
					oldUser.setRoleId(userEdit.getRoleId());
				} else
					throw new RoleIdNullException(UserManagementConstant.ROLE_ID_NOT_VALID);
				Optional<UserEntity> existUpdateUser = userRepository.findById(userEdit.getLastUpdatedBy());
				if (existUpdateUser.isPresent()) {

					oldUser.setLastUpdateBy(userEdit.getLastUpdatedBy());
				} else
					throw new UserNotFoundException(UserManagementConstant.ENTER_VALID_ID_OF_UPDATING_USER);
				oldUser.setLastUpdateTime(new Date());
				log.info("Service updateUser saving details to database");
				UserEntity savedUser = userRepository.save(oldUser);
				if (ObjectUtils.isNotEmpty(savedUser)) {
					log.info("Service updateUser ends with response savedUser: {}", savedUser);
					return savedUser;
				} else {
					log.error("Service updateUser user has not saved");
					throw new MongoException(UserManagementConstant.USER_NOT_SAVED);
				}
			}
		} else {
			log.error("Service update User does not found");
			throw new UserNotFoundException(UserManagementConstant.USER_DOES_NOT_FOUND);
		}

	}

	/**
	 * Description : Retrieving all users based on hierarchy
	 *
	 * @param : userId
	 * @throws DBException
	 * @throws :           UserNotFoundException
	 */

	@Override
	public List<UserListResponse> viewAllUserByHeirarchy(String userId) throws UserNotFoundException, MongoException {
		try {
			Optional<UserEntity> existingUser = userRepository.findById(userId);
			if (existingUser.isPresent()) {
				log.info(" Service viewAllUserByHeirarchy method starts with userId " + userId);
				List<UserListResponse> userResponseList = new ArrayList<>();
				List<UserEntity> userHierarchyList = userDaoImpl.getAllUserByHierarchy(userId);
				log.info("Userlist" + userHierarchyList);

				if (CollectionUtils.isEmpty(userHierarchyList)) {
					log.error(" Service method userhierachylist is empty ");
					throw new MongoException(UserManagementConstant.DB_EXCEPTION);
				} else {
					userHierarchyList.stream().filter(user -> !user.getUserId().equalsIgnoreCase(userId))
							.collect(Collectors.toList()).forEach(eachUser -> {
								UserListResponse response = new UserListResponse();
								response.setUserId(eachUser.getUserId());
								response.setUserName(
										eachUser.getFirstName().concat(" ").concat(eachUser.getLastName()));
								response.setRole(eachUser.getRoleId());
								userResponseList.add(response);
							});
					log.info(" End of viewAllUserByHeirarchy method ");
					return userResponseList;
				}

			} else
				throw new UserNotFoundException(UserManagementConstant.USER_NOT_FOUND);
		} catch (Exception ex) {
			throw new UserNotFoundException(UserManagementConstant.USER_NOT_FOUND);

		}
	}

	/**
	 * Description : Retrieving the user by providing user first name
	 *
	 * @param : firstName
	 * @throws UserNotFoundException
	 * @throws :DBException
	 */

	@Override
	public List<UserListResponse> findByFirstName(String firstName) throws MongoException, UserNotFoundException {
		try {
			log.info("Service findByfirstName method starts with firstName:" + firstName);
			List<UserEntity> userList = userDaoImpl.getUserByFirstName(firstName);
			List<UserListResponse> userResponseList = new ArrayList<>();
			if (CollectionUtils.isEmpty(userList)) {
				throw new MongoException(UserManagementConstant.DB_EXCEPTION);
			} else {
				userList.stream().filter(user -> !user.getUserId().equalsIgnoreCase(firstName))
						.collect(Collectors.toList()).forEach(eachUser -> {
							UserListResponse response = new UserListResponse();
							response.setUserId(eachUser.getUserId());
							response.setUserName(eachUser.getFirstName().concat(" ").concat(eachUser.getLastName()));
							response.setRole(eachUser.getRoleId());
							userResponseList.add(response);
						});
				log.info(" End of findByFirstName method ");
				return userResponseList;
			}
		} catch (Exception ex) {
			throw new UserNotFoundException(UserManagementConstant.USER_NOT_FOUND);
		}

	}

	/**
	 * This is the method for delete the user
	 * 
	 * @param userIds
	 * @return user
	 */
	@Override
	public ArrayList<ErrorTextResponse> deleteUser(List<String> userIds) {
		ArrayList<ErrorTextResponse> errorList = new ArrayList<ErrorTextResponse>();
		try {
			if (!userIds.isEmpty()) {
				userIds.forEach(userId -> {

					if (!userId.isEmpty()) {
						Optional<UserEntity> user = userRepository.findById(userId);
						if (user.isPresent()) {
							userRepository.deleteById(userId);
						} else {
							ErrorTextResponse error = new ErrorTextResponse();
							error.setErrorElement(userId);
							error.setErrorDescription(UserManagementConstant.USER_ID_NOT_AVAILABLE);
							errorList.add(error);
						}
					} else {
						ErrorTextResponse error = new ErrorTextResponse();
						error.setErrorDescription(UserManagementConstant.USER_ID_INVALID);
						errorList.add(error);
					}
				});
			} else {
				ErrorTextResponse error = new ErrorTextResponse();
				error.setErrorDescription(UserManagementConstant.USER_ID_LIST_INVALID);
				errorList.add(error);
			}
		} catch (Exception e) {
			throw new MongoException(UserManagementConstant.DB_EXCEPTION);
		}
		return errorList;
	}

	/**
	 *Method to view user by userId
	 *@param userId
	 *@return userResponse
	 */
	@Override
	public UserResponse viewUser(String userId) {
		log.info("UserServiceImpl viewUser starts with userId {}", userId);
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(UserManagementConstant.USER_DOES_NOT_FOUND));
		UserResponse userResponse=modelMapper.map(user, UserResponse.class);
		log.info("UserServiceImpl viewUser ends with userId {}", userId);
		return userResponse;
		
	}
}