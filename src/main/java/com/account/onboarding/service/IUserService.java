package com.account.onboarding.service;

import java.util.ArrayList;
import java.util.List;

import com.account.onboarding.entity.UserEntity;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;
import com.account.onboarding.response.ErrorTextResponse;
import com.account.onboarding.response.UserListResponse;
import com.account.onboarding.response.UserResponse;

/**
 * This is the service interface of UserManagement
 * 
 * @author
 *
 */
public interface IUserService {
	public UserEntity addUser(UserRequest userRequest);

	public UserEntity updateUser(UserEditRequest userEdit);

	public List<UserListResponse> findByFirstName(String firstName);

	public List<UserListResponse> viewAllUserByHeirarchy(String userId);

	public ArrayList<ErrorTextResponse> deleteUser(List<String> userIds);

	public UserResponse viewUser(String userId);
}