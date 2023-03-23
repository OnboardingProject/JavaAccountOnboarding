package com.account.onboarding.constants;

/**
 * Class for declaring constants associated with Account
 *
 */
public class AccountOverviewConstant {

	public static final String SAVE_SUCCESS = "The details saved successfully";
	public static final String SAVE_FAILED = "Failed to Save Details";
	public static final String UPDATE_SUCCESS = "The details updated successfully";
	public static final String UPDATE_FAILED = "Failed to update Details";
	public static final String GET_SUCCESS = "The details returned successfully";
	public static final String GET_FAILED = "Failed to Get Details";
	public static final String NO_SUCH_VALUE = "No such value present in db";
	public static final String DATA_PRESENT = "Details added already. Try to update it..";
	public static final String ID_NOT_FOUND = "No such Id found in db";
	public static final String NULL_VALUES = "Values are null";
	public static final String EXISTING_ENGAGEMENT_NAME = "This Engagement name is Existing";
	public static final String NO_ACCOUNT_DETAILS = "Account Details Not Found";
	public static final String NO_CUSTOMER_DETAILS = "Customer Details Not Found";
	public static final String NO_ENGAGEMENT_DETAILS = "Engagement Details Not Found";
	public static final String UPDATE_FAILED_EMPTY_LIST = "Failed to update.. Engagements not Found";
	public static final String UPDATE_FAILED_NOT_PRESENT = "Failed to Update.. Requested Engagement name not Present";

	public static final String VIEW_SUCCESS = "The details viewed successfully";
	public static final String VIEW_FAILED = "Failed to view details";
	public static final String SAVE_START = "Save started";
	public static final String SAVE_END = "Save ended";
	public static final String VIEW_START = "View started";
	public static final String VIEW_END = "View ended";
	public static final String VALIDATION_MSG = "Overview should be minimum 5 characters";
	public static final String UPDATE_START = "Update started";
	public static final String INITIATIVE_EXCEPTION = "Initiative exception thrown";
	public static final String UPDATE_END = "Update ended";
	public static final String EXISTING_INITIATIVE_NAME = "This Initiative name is Existing";
	public static final String ACCOUNT_OVERVIEW="account-overview";
	public static final String ACCOUNT="/account";
	public static final String ACCOUNT_SUMMARY="Create an account";
	public static final String ACCOUNT_DESCRIPTION="This API is used to create an account";
	public static final String ABOUT_CUSTOMER ="/account/about-customer";
	public static final String ADD_CUSTOMER_SUMMARY="Add about customer";
	public static final String CUSTOMER_DESCRIPTION="This API is used to add about customer";
	public static final String ACCOUNT_ENGAGEMENT="/account/engagement";
	public static final String ENGAGEMENT_SUMMARY="create Customer Engagements";
	public static final String ENGAGEMENT_DESCRIPTION="This API is used to create customer engagements";
	public static final String GET_ALL_ACCOUNT_SUMMARY="Get all accounts";
	public static final String GET_ALL_ACCOUNT_DESCRIPTION="This API is used to get all accounts";
	public static final String ABOUT_CUSTOMER_BY_ACCOUNT_ID="/account/about-customer/{accountId}";
	public static final String ABOUT_CUSTOMER_SUMMARY="View details of About Customer";
	public static final String ABOUT_CUSTOMER_DESCRIPTION="This API is used to view details of About Customer";
	public static final String ACCOUNT_ENGAGEMENTS_BY_ACCOUNT_ID="/account/engagement/{accountId}";
	public static final String ACCOUNT_ENGAGEMENT_SUMMARY="View Customer Engagements";
	public static final String ACCOUNT_ENGAGEMENT_DESCRIPTION="This API is used to view customer engagements";
	public static final String ACCOUNT_ID="accountId";
	public static final String CUSTOMER_UPDATE_SUMMARY="Update details of About Customer";
	public static final String CUSTOMER_UPDATE_DESCRIPTION="This API is used to update details of about customer";
	public static final String ENGAGEMENTS_UPDATE_SUMMARY="Update Customer Engagements";
	public static final String ENGAGEMENTS_UPDATE_DESCRIPTION="This API is used to update customer engagements";
	public static final String ADD_ACCOUNT_OVERVIEW="Add Account Overview";
	public static final String ADD_ACCOUNT_OVERVIEW_DESCRIPTION="This API is used to add account overview";
	public static final String OVERVIEW_UPDATE_SUMMARY="Update Account Overview";
	public static final String OVERVIEW_UPDATE_DESCRIPTION= "This API is used to update account overview";
	public static final String OVERVIEW_BY_ACCOUNT_ID= "/{accountId}";
	public static final String OVERVIEW_BY_ACCOUNT_ID_SUMMARY= "View Account Overview";
	public static final String OVERVIEW_BY_ACCOUNT_ID_DESCRIPTION= "This API is used to view account overview";
	public static final String INITIATIVES= "/account/initiatives";
	public static final String INITIATIVES_SUMMARY="Save Initiatives";
	public static final String INITIATIVES_DESCRIPTION="This API is used to save initiatives";
	public static final String INITIATIVES_UPDATE_SUMMARY="Update Initiatives";
	public static final String INITIATIVES_UPDATE_DESCRIPTION="This API is used to update initiatives";
	public static final String GET_INITIATIVES_BY_ACCOUNT_ID="/account/initiatives/{accountId}";
	public static final String GET_INITIATIVES_SUMMARY="View Initiatives";
	public static final String GET_INITIATIVES_DESCRIPTION="This API is used to view initiatives";
	public static final String ACCOUNT_INITIATIVE_NOT_FOUND="Account initiative not found";
	public static final String ACCOUNT_ID_NOT_FOUND="Id Not Found";
	public static final String GET_ACCOUNT_DETAILS="/account/{accountId}";
	public static final String GET_ACCOUNT_DETAILS_SUMMARY= "View account details";
	public static final String GET_ACCOUNT_DETAILS_DESCRIPTION= "This API is used to view account details";
	public static final String ACCOUNT_TASK="/account/tasks";
	public static final String ACCOUNT_TASK_SUMMARY="Save tasks";
	public static final String ACCOUNT_TASK_DESCRIPTION="This API is used to save tasks";
	public static final String TASK_ALEARDY_EXISTS="Task name already exists";
	public static final String ACCOUNT_EDIT_TASK_SUMMARY="Edit tasks";
	public static final String ACCOUNT_EDIT_TASK_DESCRIPTION="This API is used to edit tasks";
}