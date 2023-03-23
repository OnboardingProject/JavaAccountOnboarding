package com.account.onboarding.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import com.account.onboarding.constants.UserManagementConstant;
import com.account.onboarding.exception.EmailNotValidException;
import com.account.onboarding.exception.PhoneNumberNotValidException;
import com.account.onboarding.exception.RoleIdNullException;
import com.account.onboarding.exception.UserNotFoundException;
import com.account.onboarding.exception.UserUpdateException;
import com.account.onboarding.request.UserEditRequest;
import com.account.onboarding.request.UserRequest;

/**
 * This is validation class for UserManagement
 * 
 *
 */
@Configuration
public class UserValidation {

	public Boolean validateUser(UserRequest User) {
		boolean isValid = true;
		if (User == null) {
			isValid = false;

		} else if (User.getUserName() == null || User.getUserName().isEmpty()) {
			isValid = false;

		} else if (User.getEmailId() == null || User.getEmailId().isEmpty()) {
			isValid = false;

			throw new EmailNotValidException(UserManagementConstant.EMAIL_ID_NOT_VALID);
		} else if (User.getPhoneNumber() == null || User.getPhoneNumber().isEmpty()) {
			isValid = false;

			throw new PhoneNumberNotValidException(UserManagementConstant.PHONE_NUMBER_ID_NOT_VALID);
		}

		return isValid;

	}

	public boolean phoneValidation(UserRequest userRequest) {
		String phone = userRequest.getPhoneNumber();
		String regex = UserManagementConstant.PHONE_NUMBER_REGEX;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phone);
		if (matcher.matches()) {
			return true;
		} else
			throw new UserNotFoundException(UserManagementConstant.PHONE_NUMBER_NOT_VALID);
	}

	public boolean emailValidation(UserRequest userRequest) {
		String emailId = userRequest.getEmailId();
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailId);
		if (matcher.matches())
			return true;
		else
			throw new UserNotFoundException(UserManagementConstant.EMAIL_NOT_VALID);
	}

	/**
	 * @param userEdit
	 * @return boolean
	 */
	public boolean updateUserValidation(UserEditRequest userEdit) {
		String userId = userEdit.getUserId();
		String lastUpdatedBy = userEdit.getLastUpdatedBy();
		Integer roleId = userEdit.getRoleId();
		Pattern pattern = Pattern.compile(UserManagementConstant.USER_ID_CHECK, Pattern.CASE_INSENSITIVE);
		Matcher matcherUser = pattern.matcher(userId);
		Matcher matcherUpdater = pattern.matcher(lastUpdatedBy);
		boolean idCheck = matcherUser.find();
		boolean updaterCheck = matcherUpdater.find();

		if (StringUtils.isEmpty(userEdit.getUserId()) || idCheck)

			throw new UserUpdateException(UserManagementConstant.ENTER_VALID_USER_ID);
		else if (roleId <= 0) {
			throw new RoleIdNullException(UserManagementConstant.ENTER_VALID_ROLE_ID);
		} else if (StringUtils.isEmpty(userEdit.getLastUpdatedBy()) || updaterCheck) {
			throw new UserUpdateException(UserManagementConstant.ENTER_VALID_CREATED_BY_ID);
		} else
			return true;

	}
}