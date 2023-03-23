package com.account.onboarding.constants;

/**
 * Class which declared constant variables which are related to user management
 *
 */
public class UserManagementConstant {
	
	public static final String USER = "/user";
	public static final String ADD_USER = "/add";
	public static final String ADD_USER_SUMMARY="Adding User";
	public static final String ADD_USER_DESCRIPTION="API for adding the details of a user";
	public static final String EDIT_USER = "/update";
	public static final String EDIT_USER_SUMMARY ="Updating User";
	public static final String EDIT_USER_DESCRIPTION ="API for updating the role details of a user";
	public static final String GET_USERS_BY_ID="/views/{userId}";
	public static final String GET_USERS_BY_ID_SUMMARY="View users by hierarchy";
	public static final String GET_USERS_BY_ID_DESCRIPTION="This API is used to fetch all users with few info";
	public static final String GET_USERS_BY_NAME="/search/{firstName}";
	public static final String GET_USERS_BY_NAME_SUMMARY="Find user by first name";
	public static final String GET_USERS_BY_NAME_DESRIPTION="This API is used to fetch the perticular user";
	public static final String FIRST_NAME="firstName";
	public static final String DELETE_USER="/deleteUser";
	public static final String DELETE_USER_SUMMARY="Delete a list of users";
	public static final String DELETE_USER_DESCRIPTION="This API is used to delete list of users";
	public static final String DELETE_USER_RESPONSE="UserIds deleted Successfully";
	public static final String ROLE_SAME_WITH_USER="The Role is same with the user ";
	public static final String ROLE_ID_NOT_VALID="RoleId is not valid";
	public static final String ENTER_VALID_ID_OF_UPDATING_USER="Enter valid userId of the updating user";
	public static final String USER_NOT_SAVED="User has not been saved";
	public static final String USER_DOES_NOT_FOUND="User doesn't found";
	public static final String DB_EXCEPTION="unknown exception from Db ";
	public static final String USER_NOT_FOUND="User not found";
	public static final String USER_ID_NOT_AVAILABLE="UserId is Not available in db";
	public static final String USER_ID_INVALID="UserId is Invalid";
	public static final String USER_ID_LIST_INVALID="UserId list is invalid";
	public static final String ACCOUNT_ID="accountId";
	public static final String USER_ID="userId";
	public static final String TYPE_ID="typeId";
	public static final String HIERARCHY="hierarchy";
	public static final String EMAIL_ID_NOT_VALID="Email id is not valid";
	public static final String PHONE_NUMBER_ID_NOT_VALID="this number is not valid";
	public static final String PHONE_NUMBER_REGEX="\\d{10}$";
	public static final String PHONE_NUMBER_NOT_VALID="phone number is not valid";
	public static final String EMAIL_NOT_VALID="emailId is not valid";
	public static final String USER_ID_CHECK="[^a-z0-9]";
	public static final String ENTER_VALID_USER_ID="Enter valid userId ";
	public static final String ENTER_VALID_ROLE_ID="Enter valid roleId ";
	public static final String ENTER_VALID_CREATED_BY_ID="Enter valid userId of the updating user";
	public static final String USER_ID_URL ="/{userId}";
	
}
