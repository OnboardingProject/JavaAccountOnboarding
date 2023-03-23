package com.account.onboarding.dao;

import java.util.List;

import com.account.onboarding.entity.UserEntity;

/**
 * This is the dao interface of UserManagement where we declare methods to get
 * hierarchy of user and to check whether roleId exists
 * 
 * @author
 *
 */
public interface IUserDao {

	String getUser(String accountName, String createdBy);

	Integer getRoleId(Integer roleId);

	List<UserEntity> getAllUserByHierarchy(String userId);

	List<UserEntity> getUserByFirstName(String firstName);
}
