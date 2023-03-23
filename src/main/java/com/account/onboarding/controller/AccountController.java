package com.account.onboarding.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.AccountOverviewConstant;
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
import com.account.onboarding.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * The class that handles all the api requests related to Account
 * 
 * @author 226732
 *
 */
 
@Slf4j
@RestController
@RequestMapping(AccountOverviewConstant.ACCOUNT_OVERVIEW)
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	

	/**
	 * Method to add the account details
	 * 
	 * @param account
	 * @return saved account
	 */
	@PostMapping(AccountOverviewConstant.ACCOUNT)
	@Operation(summary = AccountOverviewConstant.ACCOUNT_SUMMARY, description = AccountOverviewConstant.ACCOUNT_DESCRIPTION)
	public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
		log.info("AccountController createAccount starts with {}", accountRequest);
		AccountResponse savedAccount = accountService.createAccount(accountRequest);
		log.info("AccountController createAccount ends with {}", savedAccount);
		return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
	}

	/**
	 * Method to save description about customer
	 * 
	 * @param aboutCustomer
	 * @return responseEntity
	 */
	@PostMapping(AccountOverviewConstant.ABOUT_CUSTOMER)
	@Operation(summary = AccountOverviewConstant.ADD_CUSTOMER_SUMMARY, description = AccountOverviewConstant.CUSTOMER_DESCRIPTION)
	public ResponseEntity<AboutCustomerResponse> addAboutCustomer(
			@Valid @RequestBody AboutCustomerRequest aboutCustomerRequest) {
		log.info("AccountController addAboutCustomer starts with {}", aboutCustomerRequest);
		AboutCustomerResponse aboutCustomerResponse = accountService.addAboutCustomer(aboutCustomerRequest);
		log.info("AccountController addAboutCustomer ends with {}", aboutCustomerResponse);
		return new ResponseEntity<>(aboutCustomerResponse, HttpStatus.CREATED);
	}

	/**
	 * Method to add customer engagement
	 * @param customerEngagementRequest
	 * @return
	 */
	@PostMapping(AccountOverviewConstant.ACCOUNT_ENGAGEMENT)
	@Operation(summary = AccountOverviewConstant.ENGAGEMENT_SUMMARY, description = AccountOverviewConstant.ENGAGEMENT_DESCRIPTION)
	public ResponseEntity<List<CustomerEngagement>> createCustomerEngagement(
			@Valid @RequestBody CustomerEngagementRequest customerEngagementRequest) {
		log.info("AccountController createCustomerEngagement starts with {}", customerEngagementRequest);
		List<CustomerEngagement> customerEngagementList = accountService
				.addCustomerEngagements(customerEngagementRequest);
		log.info("AccountController createCustomerEngagement Controller ends with {}", customerEngagementList);
		return new ResponseEntity<>(customerEngagementList, HttpStatus.CREATED);
	}

	/**
	 * Method to get all account details
	 * 
	 * @return List of Accounts
	 */
	@GetMapping(AccountOverviewConstant.ACCOUNT)
	@Operation(summary = AccountOverviewConstant.GET_ALL_ACCOUNT_SUMMARY, description = AccountOverviewConstant.GET_ALL_ACCOUNT_DESCRIPTION)
	public ResponseEntity<List<Account>> getAllAccounts() {
		log.info("AccountController getAllAccounts starts");
		List<Account> accounts = accountService.getAllAccounts();
		log.info("AccountController getAllAccounts ends");
		return new ResponseEntity<>(accounts, HttpStatus.OK);
	}

	/**
	 * Method to save description about customer
	 * 
	 * @param accountId
	 * @return aboutCustomerResponse
	 */
	@GetMapping(AccountOverviewConstant.ABOUT_CUSTOMER_BY_ACCOUNT_ID)
	@Operation(summary = AccountOverviewConstant.ABOUT_CUSTOMER_SUMMARY, description = AccountOverviewConstant.ABOUT_CUSTOMER_DESCRIPTION)
	public ResponseEntity<AboutCustomerResponse> viewAboutCustomer(
			@PathVariable(AccountOverviewConstant.ACCOUNT_ID) String accountId) {
		log.info("AccountController viewAboutCustomer starts with {}", accountId);
		AboutCustomerResponse aboutCustomerResponse = accountService.viewAboutCustomer(accountId);
		log.info("AccountController viewAboutCustomer ends with {}", aboutCustomerResponse);
		return new ResponseEntity<>(aboutCustomerResponse, HttpStatus.OK);
	}

	/**
	 * Method to get customer engagements
	 * @param accountId
	 * @return
	 */
	@GetMapping(AccountOverviewConstant.ACCOUNT_ENGAGEMENTS_BY_ACCOUNT_ID)
	@Operation(summary = AccountOverviewConstant.ACCOUNT_ENGAGEMENT_SUMMARY, description = AccountOverviewConstant.ACCOUNT_ENGAGEMENT_DESCRIPTION)
	public ResponseEntity<List<CustomerEngagement>> viewCustomerEngagements(
			@PathVariable(AccountOverviewConstant.ACCOUNT_ID) String accountId) {
		log.info("AccountController viewCustomerEngagements starts with accountId {}", accountId);
		List<CustomerEngagement> customerEngagementList = accountService.viewCustomerEngagements(accountId);
		log.info("AccountController viewCustomerEngagements ends with accountId {}", accountId);
		return new ResponseEntity<>(customerEngagementList, HttpStatus.OK);
	}

	/**
	 * Method to edit- about customer
	 * @param aboutCustomer
	 * @return aboutCustomerResponse
	 */
	@PutMapping(AccountOverviewConstant.ABOUT_CUSTOMER)
	@Operation(summary = AccountOverviewConstant.CUSTOMER_UPDATE_SUMMARY, description = AccountOverviewConstant.CUSTOMER_UPDATE_DESCRIPTION)
	public ResponseEntity<AboutCustomerResponse> updateAboutCustomer(
			@Valid @RequestBody AboutCustomerRequest aboutCustomerRequest) {
		log.info("AccountController updateAboutCustomer starts with {}", aboutCustomerRequest);
		AboutCustomerResponse aboutCustomerResponse = accountService.updateAboutCustomer(aboutCustomerRequest);
		log.info("AccountController updateAboutCustomer ends with {}", aboutCustomerResponse);
		return new ResponseEntity<>(aboutCustomerResponse, HttpStatus.OK);
	}

	/**
	 * Method to edit customer engagement
	 * @param customerEngagementRequest
	 * @return
	 */
	@PutMapping(AccountOverviewConstant.ACCOUNT_ENGAGEMENT)
	@Operation(summary = AccountOverviewConstant.ENGAGEMENTS_UPDATE_SUMMARY, description = AccountOverviewConstant.ENGAGEMENTS_UPDATE_DESCRIPTION)
	public ResponseEntity<List<CustomerEngagement>> updateCustomerEngagements(
			@Valid @RequestBody CustomerEngagementRequest customerEngagementRequest) {
		log.info("AccountController updateCustomerEngagements starts with {}", customerEngagementRequest);
		List<CustomerEngagement> customerEngagementList = accountService
				.updateCustomerEngagements(customerEngagementRequest);
		log.info("AccountController updateCustomerEngagements controller ends with {}", customerEngagementList);
		return new ResponseEntity<>(customerEngagementList, HttpStatus.OK);
	}

	/**
	 * accountSave is used to add account overview description
	 * 
	 * @param account
	 * @return accountDescription
	 */

	@PostMapping()
	@Operation(summary = AccountOverviewConstant.ADD_ACCOUNT_OVERVIEW, description = AccountOverviewConstant.ADD_ACCOUNT_OVERVIEW_DESCRIPTION)
	public ResponseEntity<String> addAccountOverview(@RequestBody @Valid AccountDetailsRequest account) {
		if (account.getAccountId() != null) {
			String accountDescription = accountService.updateOverview(account);
			log.info(AccountOverviewConstant.SAVE_SUCCESS);
			return new ResponseEntity<>(accountDescription, HttpStatus.OK);
		} else {
			log.debug(AccountOverviewConstant.SAVE_FAILED);
			return new ResponseEntity<>(AccountOverviewConstant.SAVE_FAILED, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * updateOverview method is to update account overview
	 * 
	 * @param account
	 * @return updatedAccount
	 */
	@PutMapping()
	@Operation(summary = AccountOverviewConstant.OVERVIEW_UPDATE_SUMMARY, description = AccountOverviewConstant.OVERVIEW_UPDATE_DESCRIPTION)
	public ResponseEntity<String> updateOverview(@RequestBody @Valid AccountDetailsRequest account) {

		if (account.getAccountId() != null) {
			String updatedAccount = accountService.updateOverview(account);
			log.info(AccountOverviewConstant.UPDATE_SUCCESS);
			return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
		} else {
			log.debug(AccountOverviewConstant.UPDATE_FAILED);
			return new ResponseEntity<>(AccountOverviewConstant.UPDATE_FAILED, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * viewAccount method is used to view Account Overview
	 * 
	 * @param accountId
	 * @return accountOverview
	 */
	@GetMapping(AccountOverviewConstant.OVERVIEW_BY_ACCOUNT_ID)
	@Operation(summary = AccountOverviewConstant.OVERVIEW_BY_ACCOUNT_ID_SUMMARY, description = AccountOverviewConstant.OVERVIEW_BY_ACCOUNT_ID_DESCRIPTION)
	public ResponseEntity<String> viewAccountOverview(@PathVariable String accountId) {

		String accountOverview = accountService.viewAccountOverview(accountId);
		log.info(AccountOverviewConstant.VIEW_SUCCESS);
		return new ResponseEntity<>(accountOverview, HttpStatus.OK);

	}

	/**
	 * saveInitiatives is used to save initiatives of the account
	 * 
	 * @param initiatives
	 * @return initiatives
	 */

	@PostMapping(AccountOverviewConstant.INITIATIVES)
	@Operation(summary = AccountOverviewConstant.INITIATIVES_SUMMARY, description = AccountOverviewConstant.INITIATIVES_DESCRIPTION)
	public ResponseEntity<List<AccountInitiative>> saveInitiatives(@RequestBody InitiativesRequest initiativesRequest) {
		if (initiativesRequest.getAccountId() != null) {
			List<AccountInitiative> initiatives = accountService.saveInitiatives(initiativesRequest);
			log.info(AccountOverviewConstant.SAVE_SUCCESS);
			return new ResponseEntity<List<AccountInitiative>>(initiatives, HttpStatus.CREATED);
		} else {
			log.debug(AccountOverviewConstant.SAVE_FAILED);
			return new ResponseEntity<List<AccountInitiative>>(HttpStatus.BAD_REQUEST);
		}

	}
	/**
	 * saveTasks is used to save tasks of the account
	 * 
	 * @param Tasks
	 * @return TaskList
	 */

	@PostMapping(AccountOverviewConstant.ACCOUNT_TASK)
	@Operation(summary = AccountOverviewConstant.ACCOUNT_TASK_SUMMARY, description = AccountOverviewConstant.ACCOUNT_TASK_DESCRIPTION)
	public ResponseEntity<AccountTaskResponse> saveTasks(@RequestBody AccountTaskRequest accounttaskRequest) {
		if (accounttaskRequest.getAccountId() != null) {
			AccountTaskResponse tasks = accountService.saveTasks(accounttaskRequest);
			log.info(AccountOverviewConstant.SAVE_SUCCESS);
			return new ResponseEntity<AccountTaskResponse>(tasks, HttpStatus.CREATED);
		} else {
			log.debug(AccountOverviewConstant.SAVE_FAILED);
			return new ResponseEntity<AccountTaskResponse>(HttpStatus.BAD_REQUEST);
		}

	}
	/**
	 * editTasks is used to edit tasks of the account
	 * 
	 * @param Tasks
	 * @return TaskList
	 */

	@PutMapping(AccountOverviewConstant.ACCOUNT_TASK)
	@Operation(summary = AccountOverviewConstant.ACCOUNT_EDIT_TASK_SUMMARY, description = AccountOverviewConstant.ACCOUNT_EDIT_TASK_DESCRIPTION)
	public ResponseEntity<List<TaskDetails>> saveTaskStatus(@RequestBody AccountTaskStatusRequest accountTaskStatus) {
		if (accountTaskStatus.getAccountId() != null) {
			List<TaskDetails> tasks = accountService.saveAccountStatus(accountTaskStatus);
			log.info(AccountOverviewConstant.SAVE_SUCCESS);
			return new ResponseEntity<List<TaskDetails>>(tasks, HttpStatus.CREATED);
		} else {
			log.debug(AccountOverviewConstant.SAVE_FAILED);
			return new ResponseEntity<List<TaskDetails>>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * updateInitiatives used to update any initiatives of the account
	 * 
	 * @param initiatives
	 * @return updatedInitiatives
	 */
	@PutMapping(AccountOverviewConstant.INITIATIVES)
	@Operation(summary = AccountOverviewConstant.INITIATIVES_UPDATE_SUMMARY, description = AccountOverviewConstant.INITIATIVES_UPDATE_DESCRIPTION)
	public ResponseEntity<List<AccountInitiative>> updateInitiative(@RequestBody InitiativesRequest initiatives) {
		if (initiatives.getAccountId() != null) {
			List<AccountInitiative> updatedInitiatives = accountService.updateInitiatives(initiatives);
			log.info(AccountOverviewConstant.UPDATE_SUCCESS);
			return new ResponseEntity<List<AccountInitiative>>(updatedInitiatives, HttpStatus.OK);
		} else {
			log.info(AccountOverviewConstant.UPDATE_FAILED);
			return new ResponseEntity<List<AccountInitiative>>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * viewInitiatives used to view initiatives of the account
	 * 
	 * @param accountId
	 * @return view
	 */

	@GetMapping(AccountOverviewConstant.GET_INITIATIVES_BY_ACCOUNT_ID)
	@Operation(summary = AccountOverviewConstant.GET_INITIATIVES_SUMMARY, description = AccountOverviewConstant.GET_INITIATIVES_DESCRIPTION)
	public ResponseEntity<List<AccountInitiative>> viewInitiatives(@PathVariable String accountId) {

		List<AccountInitiative> view = accountService.viewInitiatives(accountId);
		log.info(AccountOverviewConstant.VIEW_SUCCESS);
		return new ResponseEntity<>(view, HttpStatus.OK);

	}
	/**
	 * View Account details used to view account details of the given accountId
	 * 
	 * @param accountId
	 * @return view
	 */

	@GetMapping(AccountOverviewConstant.GET_ACCOUNT_DETAILS)
	@Operation(summary = AccountOverviewConstant.GET_ACCOUNT_DETAILS_SUMMARY, description = AccountOverviewConstant.GET_ACCOUNT_DETAILS_DESCRIPTION)
	public ResponseEntity<AccountResponse> viewAccountByAccountId(@PathVariable String accountId) {
		log.info("AccountController viewAccountByAccountId starts with accountId {}", accountId);
		AccountResponse view = accountService.viewAccount(accountId);
		log.info("AccountController viewAccountByAccountId ends with accountId {}", accountId);
		return new ResponseEntity<>(view, HttpStatus.OK);

	}
}