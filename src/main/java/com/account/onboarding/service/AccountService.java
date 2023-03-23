package com.account.onboarding.service;

import java.util.List;

import com.account.onboarding.entity.Account;
import com.account.onboarding.entity.AccountInitiative;
import com.account.onboarding.entity.CustomerEngagement;
import com.account.onboarding.entity.Task;
import com.account.onboarding.entity.TaskDetails;
import com.account.onboarding.request.AboutCustomerRequest;
import com.account.onboarding.request.AccountRequest;
import com.account.onboarding.request.AccountTaskRequest;
import com.account.onboarding.request.AccountDetailsRequest;
import com.account.onboarding.request.CustomerEngagementRequest;
import com.account.onboarding.request.InitiativesRequest;
import com.account.onboarding.request.AccountTaskStatusRequest;
import com.account.onboarding.response.AboutCustomerResponse;
import com.account.onboarding.response.AccountResponse;
import com.account.onboarding.response.AccountTaskResponse;

public interface AccountService {

	public List<Account> getAllAccounts();

	public AccountResponse createAccount(AccountRequest account);

	public AboutCustomerResponse addAboutCustomer(AboutCustomerRequest aboutCustomerRequest);

	public AboutCustomerResponse viewAboutCustomer(String accountId);

	public AboutCustomerResponse updateAboutCustomer(AboutCustomerRequest aboutCustomerRequest);

	public List<CustomerEngagement> addCustomerEngagements(CustomerEngagementRequest customerEngagementRequest);

	public List<CustomerEngagement> viewCustomerEngagements(String accountId);

	public List<CustomerEngagement> updateCustomerEngagements(CustomerEngagementRequest customerEngagementRequest);

	public String updateOverview(AccountDetailsRequest account);

	public String viewAccountOverview(String accountId);

	public List<AccountInitiative> saveInitiatives(InitiativesRequest initiatives);

	public List<AccountInitiative> viewInitiatives(String accountId);

	public List<AccountInitiative> updateInitiatives(InitiativesRequest initiatives);

	public AccountResponse viewAccount(String accountId);

	public AccountTaskResponse saveTasks(AccountTaskRequest accounttaskRequest);

	public List<TaskDetails> saveAccountStatus(AccountTaskStatusRequest accountTaskStatus);

}