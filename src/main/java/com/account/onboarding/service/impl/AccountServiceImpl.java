package com.account.onboarding.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.account.onboarding.constants.AccountOverviewConstant;
import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.entity.Account;
import com.account.onboarding.entity.AccountInitiative;
import com.account.onboarding.entity.CustomerEngagement;
import com.account.onboarding.entity.ProjectTaskDetails;
import com.account.onboarding.entity.Task;
import com.account.onboarding.entity.TaskDetails;
import com.account.onboarding.entity.UserEntity;
import com.account.onboarding.exception.AccountCustomerException;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.repository.AccountRepository;
import com.account.onboarding.repository.TaskRepository;
import com.account.onboarding.request.AboutCustomerRequest;
import com.account.onboarding.request.AccountRequest;
import com.account.onboarding.request.AccountTaskRequest;
import com.account.onboarding.request.AccountDetailsRequest;
import com.account.onboarding.request.CustomerEngagementRequest;
import com.account.onboarding.request.InitiativesRequest;
import com.account.onboarding.request.AccountTaskStatusRequest;
import com.account.onboarding.request.SaveTaskStatusRequest;
import com.account.onboarding.request.TaskStatusRequest;
import com.account.onboarding.response.AboutCustomerResponse;
import com.account.onboarding.response.AccountResponse;
import com.account.onboarding.response.AccountTaskResponse;
import com.account.onboarding.service.AccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * The class that handle all the account services
 * 
 * @author 226732
 *
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	SequenceGeneratorService sequenceGenerator;

	/**
	 * Method to add the account
	 *
	 * @param account
	 * @return savedAccount
	 */
	@Override
	public AccountResponse createAccount(AccountRequest accountRequest) {
		log.info("AccountService createAccount starts with {}", accountRequest); 
		if (Objects.nonNull(accountRequest)) {
			accountRequest.setAccountId(UUID.randomUUID().toString());
			Account account = new Account();
			account.setAccountId(accountRequest.getAccountId());
			account.setAccountName(accountRequest.getCustomerName());
			account.setCreatedDate(LocalDateTime.now());
			account.setCreatedBy(accountRequest.getCreatedBy());
			account.setAboutCustomer(accountRequest.getAboutCustomer());
			account.setDocuments(accountRequest.getDocuments());
			account.setEngagements(accountRequest.getEngagements());
			account.setInitiatives(accountRequest.getInitiatives());
			account.setTasks(accountRequest.getTasks());
			account.setAccountOverview(accountRequest.getAccountOverview());
			Task task= new Task();
			List<Task> taskList= new ArrayList<>();
			account.setTasks(taskList);
			Account savedaccount = accountRepository.save(account);
			AccountResponse accountResponse=modelMapper.map(savedaccount, AccountResponse.class);
			log.info("AccountService createAccount ends with {}",savedaccount);
			return accountResponse;
		} else {
			log.error("AccountService createAccount failed. Values are null");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Method to save the customer description
	 *
	 * @param customer aboutCustomerRequest
	 * @return customer aboutCustomerResponse
	 */
	@Override
	public AboutCustomerResponse addAboutCustomer(AboutCustomerRequest aboutCustomerRequest) {
		log.info("AccountService addAboutCustomer starts with {}",aboutCustomerRequest);
		if (Objects.isNull(aboutCustomerRequest)) {
			log.error("AccountService addAboutCustomer failed. Values are null");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> accountData = accountRepository.findById(aboutCustomerRequest.getAccountId());
		if (accountData.isPresent()) {
			Account account = accountData.get();
			if (Objects.nonNull(account.getAboutCustomer()) && !account.getAboutCustomer().isEmpty()) {
				log.error(
						"AccountService addAboutCustomer customer description already present.");
				throw new AccountCustomerException(AccountOverviewConstant.DATA_PRESENT, HttpStatus.CONFLICT);
			}
			account.setAboutCustomer(aboutCustomerRequest.getAboutCustomer());
			account.setUpdatedBy(aboutCustomerRequest.getUpdatedBy());
			account.setUpdatedDate(LocalDateTime.now());
			Account savedAccount = accountRepository.save(account);
			log.info("AccountService addAboutCustomer ends");
			return new AboutCustomerResponse(savedAccount.getAccountId(), savedAccount.getAboutCustomer());
		} else {
			log.error(
					"AccountService addAboutCustomer Failed to add Desciption About Customer. Given id not found");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Method to save the customer engagements
	 *
	 * @param customer customerEngagementRequest
	 * @return customer List<CustomerEngagement>
	 */
	@Override
	public List<CustomerEngagement> addCustomerEngagements(CustomerEngagementRequest customerEngagementRequest) {
		log.info("AccountService addCustomerEngagements starts with {}",customerEngagementRequest);
		if (Objects.isNull(customerEngagementRequest)) {
			log.error("AccountService addCustomerEngagements Null value");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.NO_CONTENT);
		}
		Optional<Account> accountData = accountRepository.findById(customerEngagementRequest.getAccountId());
		if (!accountData.isPresent()) {
			log.error("AccountService addCustomerEngagements Given account id not found");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		CustomerEngagement customerEngagement = new CustomerEngagement(customerEngagementRequest.getEngagementName(),
				customerEngagementRequest.getEngagementDescription());
		List<CustomerEngagement> engagementsList = account.getEngagements();
		engagementsList = Optional.ofNullable(engagementsList).orElse(new ArrayList<>());

		if (engagementsList.stream().anyMatch(
				e -> e.getEngagementName().equalsIgnoreCase(customerEngagementRequest.getEngagementName()))) {
			log.error("AccountService addCustomerEngagements Existing Engagement name");
			throw new AccountCustomerException(AccountOverviewConstant.EXISTING_ENGAGEMENT_NAME,
					HttpStatus.BAD_REQUEST);
		} else {
			engagementsList.add(customerEngagement);
		}
		account.setEngagements(engagementsList);
		account.setUpdatedBy(customerEngagementRequest.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		Account savedAccount = accountRepository.save(account);
		log.info("AccountService addCustomerEngagements ends with account {}",savedAccount);
		return savedAccount.getEngagements();

	}

	/**
	 * Method to get all the Accounts
	 * 
	 * @return list of accounts
	 */
	@Override
	public List<Account> getAllAccounts() {
		log.info("AccountService getAllAccounts starts");
		List<Account> accounts = accountRepository.findAll();
		if (Objects.nonNull(accounts) && !accounts.isEmpty()) {
			log.info("AccountService getAllAccounts ends");
			return accounts;
		} else {
			log.error("AccountService getAllAccounts Failed to get details. Account Details not Found");
			throw new AccountCustomerException(AccountOverviewConstant.NO_ACCOUNT_DETAILS, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Method to get the customer description
	 *
	 * @param accountId
	 * @return customer description
	 */
	@Override
	public AboutCustomerResponse viewAboutCustomer(@PathVariable String accountId) {
		log.info("AccountService viewAboutCustomer starts with accountId {}", accountId);
		Optional<Account> accountData = accountRepository.findById(accountId);
		if (accountData.isPresent()) {
			Account account = accountData.get();
			if (Objects.nonNull(account.getAboutCustomer()) && !account.getAboutCustomer().isEmpty()) {
				log.info("AccountService viewAboutCustomer ends");
				return new AboutCustomerResponse(account.getAccountId(), account.getAboutCustomer());
			} else {
				log.error("AccountService viewAboutCustomer Customer Details not Found");
				throw new AccountCustomerException(AccountOverviewConstant.NO_CUSTOMER_DETAILS, HttpStatus.NOT_FOUND);
			}
		} else {
			log.error("AccountService viewAboutCustomer Not found any account with given id");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Method to update the customer description
	 *
	 * @param customer aboutCustomerRequest
	 * @return customer aboutCustomerResponse
	 */
	@Override
	public AboutCustomerResponse updateAboutCustomer(@RequestBody AboutCustomerRequest aboutCustomerRequest) {
		log.info("AccountService updateAboutCustomer starts with {}",aboutCustomerRequest);
		if (Objects.isNull(aboutCustomerRequest)) {
			log.error("AccountService updateAboutCustomer Null values");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> accountData = accountRepository.findById(aboutCustomerRequest.getAccountId());
		if (accountData.isPresent()) {
			Account account = accountData.get();
			account.setAboutCustomer(aboutCustomerRequest.getAboutCustomer());
			account.setUpdatedBy(aboutCustomerRequest.getUpdatedBy());
			account.setUpdatedDate(LocalDateTime.now());
			Account updatedAccount = accountRepository.save(account);
			log.info("AccountService updateAboutCustomer ends");
			return new AboutCustomerResponse(updatedAccount.getAccountId(), updatedAccount.getAboutCustomer());
		} else {
			log.error("AccountService updateAboutCustomer Error in updating Customer Details");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Method to view customer engagements with the given accountId
	 * @param accountId
	 * @return customer engagement list
	 *
	 */
	@Override
	public List<CustomerEngagement> viewCustomerEngagements(String accountId) {
		log.info("AccountService viewCustomerEngagements starts with {}", accountId);
		if (Objects.isNull(accountId) || accountId.isBlank()) {
			log.error("AccountService viewCustomerEngagements accountId is Null or blank");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.BAD_REQUEST);
		}
		Optional<Account> accountData = accountRepository.findById(accountId);
		if (!accountData.isPresent()) {
			log.error("AccountService viewCustomerEngagements Given account id not found");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		List<CustomerEngagement> engagementsList = account.getEngagements();
		if (Objects.isNull(engagementsList) || engagementsList.isEmpty()) {
			log.error("AccountService Customer Engagements not found");
			throw new AccountCustomerException(AccountOverviewConstant.NO_ENGAGEMENT_DETAILS, HttpStatus.NOT_FOUND);
		}
		return engagementsList;
	}

	/**
	 * Method to update customer engagements
	 * @param customerengagement request
	 * @return customerengagement List
	 *
	 */
	@Override
	public List<CustomerEngagement> updateCustomerEngagements(CustomerEngagementRequest customerEngagementRequest) {
		log.info("AccountService updateCustomerEngagements starts with {}",customerEngagementRequest);
		if (Objects.isNull(customerEngagementRequest)) {
			log.error("AccountService customerEngagementRequest is Null");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.NOT_FOUND);
		}
		Optional<Account> accountData = accountRepository.findById(customerEngagementRequest.getAccountId());
		if (!accountData.isPresent()) {
			log.error("AccountService updateCustomerEngagements Given account id not found");
			throw new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		List<CustomerEngagement> engagementsList = account.getEngagements();
		if (Objects.isNull(engagementsList) || engagementsList.isEmpty()) {
			log.error("AccountService Customer Engagements not found to update");
			throw new AccountCustomerException(AccountOverviewConstant.UPDATE_FAILED_EMPTY_LIST, HttpStatus.NOT_FOUND);
		} else {
			CustomerEngagement engagement = engagementsList.stream().filter(
					c -> c.getEngagementName().equalsIgnoreCase(customerEngagementRequest.getEngagementName()))
					.findAny().orElse(null);
			if (Objects.isNull(engagement)) {
				log.error("AccountService Customer Engagements Update failed.. Requested engagement name not found");
				throw new AccountCustomerException(AccountOverviewConstant.UPDATE_FAILED_NOT_PRESENT,
						HttpStatus.BAD_REQUEST);
			} else {
				engagement.setEngagementName(customerEngagementRequest.getEngagementName());
				engagement.setEngagementDesc(customerEngagementRequest.getEngagementDescription());
			}
		}
		account.setEngagements(engagementsList);
		account.setUpdatedBy(customerEngagementRequest.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		Account updatedAccount = accountRepository.save(account);
		log.info("AccountService updateCustomerEngagements ends");
		return updatedAccount.getEngagements();
	}

	/**
	 * Method to update account overview
	 * @param accountDetails
	 * @return accountOverview
	 *
	 */
	@Override
	public String updateOverview(AccountDetailsRequest accountDetails) {
		log.info("Update Overview starts");
		Account account = accountRepository.findById(accountDetails.getAccountId())
				.orElseThrow(() -> new AccountCustomerException(AccountOverviewConstant.ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		account.setAccountOverview(accountDetails.getAccountOverview());
		account.setUpdatedBy(accountDetails.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		accountRepository.save(account);
		log.info("Exit from update overview");
		return account.getAccountOverview();
	}

	/**
	 * Method to view account overview
	 * @param accountId
	 * @return accountOverview
	 *
	 */
	@Override
	public String viewAccountOverview(String accountId) {
		log.info(AccountOverviewConstant.VIEW_START);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountCustomerException(AccountOverviewConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		log.info(AccountOverviewConstant.VIEW_END);
		return account.getAccountOverview();

	}

	/**
	 * Method to save initiatives
	 * @param initiatives
	 * @return accountinitiatives List
	 * 
	 *
	 */
	@Override
	public List<AccountInitiative> saveInitiatives(InitiativesRequest initiatives) {
		log.info(AccountOverviewConstant.SAVE_START);

		if (Objects.isNull(initiatives)) {
			log.error("AccountService saveInitiatives Null value ");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.NO_CONTENT);
		}
		Optional<Account> accountData = accountRepository.findById(initiatives.getAccountId());
		if (!accountData.isPresent()) {
			log.error("AccountService saveInitiatives Given account id not found");
			throw new AccountCustomerException(AccountOverviewConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		AccountInitiative accountInitiatives = new AccountInitiative(initiatives.getInitiativeName(),
				initiatives.getInitiativeDescription());
		List<AccountInitiative> initiativeList = account.getInitiatives();
		initiativeList = Optional.ofNullable(initiativeList).orElse(new ArrayList<>());

		if (initiativeList.stream()
				.anyMatch(e -> e.getInitiativeName().equalsIgnoreCase(initiatives.getInitiativeName()))) {
			log.error("AccountService saveInitiatives, initiative name already exists");
			throw new AccountCustomerException(AccountOverviewConstant.EXISTING_INITIATIVE_NAME,
					HttpStatus.BAD_REQUEST);
		} else {
			initiativeList.add(accountInitiatives);
		}

		account.setInitiatives(initiativeList);
		account.setUpdatedBy(initiatives.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		Account savedAccount = accountRepository.save(account);
		log.info("AccountService saveInitiatives ends with {}",savedAccount);
		return savedAccount.getInitiatives();
	}

	/**
	 * Method to view initiatives with the given accountId
	 * @param accountId
	 * @return account overview List
	 *
	 */
	@Override
	public List<AccountInitiative> viewInitiatives(String accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountCustomerException(AccountOverviewConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		return account.getInitiatives();
	}

	/**
	 *Method to update initiatives
	 *@param initiatives
	 *@return initiatives List
	 *
	 */
	@Override
	public List<AccountInitiative> updateInitiatives(InitiativesRequest initiatives) {
		log.info(AccountOverviewConstant.UPDATE_START);
		Account account = accountRepository.findById(initiatives.getAccountId())
				.orElseThrow(() -> new AccountCustomerException(AccountOverviewConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		Integer flag = 0;
		for (AccountInitiative initiative : account.getInitiatives()) {
			if (initiative.getInitiativeName().equalsIgnoreCase(initiatives.getInitiativeName())) {
				flag++;
				initiative.setInitiativeDescription(initiatives.getInitiativeDescription());
				account.setUpdatedBy(initiatives.getUpdatedBy());
				account.setUpdatedDate(LocalDateTime.now());
			}
		}
		if (flag == 0) {
			log.info(AccountOverviewConstant.INITIATIVE_EXCEPTION);
			throw new AccountCustomerException(AccountOverviewConstant.ACCOUNT_INITIATIVE_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		accountRepository.save(account);
		log.info(AccountOverviewConstant.UPDATE_END);
		return account.getInitiatives();
	}

	/**
	 *Method to fetch account details with the given accountId
	 *@param accountId
	 *@return accountResponse
	 */
	@Override
	public AccountResponse viewAccount(String accountId) {
		log.info("AccountServiceImpl viewAccount starts with accountId {}", accountId);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountCustomerException(AccountOverviewConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
		AccountResponse accountResponse=modelMapper.map(account, AccountResponse.class);
		log.info("AccountServiceImpl viewAccount ends with accountId {}", accountId);
		return accountResponse;
	}

	/**
	 *
	 *Method to add task to the account
	 *@param accounttaskRequest
	 *@return taskResponse
	 */
	@Override
	public AccountTaskResponse saveTasks(AccountTaskRequest accounttaskRequest) {
		if (Objects.isNull(accounttaskRequest)) {
			log.error("AccountService saveTasks Null value ");
			throw new AccountCustomerException(AccountOverviewConstant.NULL_VALUES, HttpStatus.NO_CONTENT);
		}
		Optional<Account> accountData = accountRepository.findById(accounttaskRequest.getAccountId());
		if (!accountData.isPresent()) {
			log.error("AccountService saveTasks Given account id not found");
			throw new AccountCustomerException(AccountOverviewConstant.ACCOUNT_ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
		Account account = accountData.get();
		Task task = new Task();
		task.setTaskId(sequenceGenerator.generateSequence(Task.SEQUENCE_NAME));
		task.setTaskName(accounttaskRequest.getTask().getTaskName());
		task.setTaskDesc(accounttaskRequest.getTask().getTaskDesc());
		task.setTaskType(accounttaskRequest.getTask().getTaskType());
		task.setCompletionDays(accounttaskRequest.getTask().getCompletionDays());
		task.setDesignation(accounttaskRequest.getTask().getDesignation());
		task.setTaskDocuments(accounttaskRequest.getTask().getTaskDocuments());
		taskRepository.save(task);
		List<Task> taskList = account.getTasks();
		taskList = Optional.ofNullable(taskList).orElse(new ArrayList<>());

		if (taskList.stream()
				.anyMatch(e -> e.getTaskName().equalsIgnoreCase(accounttaskRequest.getTask().getTaskName()))) {
			log.error("AccountService saveTasks, Task name already exists");
			throw new AccountCustomerException(AccountOverviewConstant.TASK_ALEARDY_EXISTS,
					HttpStatus.BAD_REQUEST);
		} else {
			taskList.add(task);
		}

		account.setTasks(taskList);
		account.setUpdatedBy(accounttaskRequest.getUpdatedBy());
		account.setUpdatedDate(LocalDateTime.now());
		Account savedAccount = accountRepository.save(account);
		AccountTaskResponse taskResponse=new AccountTaskResponse();
		taskResponse=modelMapper.map(savedAccount, AccountTaskResponse.class);
		//To update in user class
		Query query =new Query();
				query.addCriteria(Criteria.where("designation")
				.in(accounttaskRequest.getTask().getDesignation())
				.andOperator(Criteria.where("accountId")
						.in(accounttaskRequest.getAccountId())));

		List<UserEntity> users = mongoTemplate.find(query, UserEntity.class);
		TaskDetails taskDetails = new TaskDetails();
		List<TaskDetails> userTaskList = new ArrayList<TaskDetails>();
		for (UserEntity user : users) {
			if (user.getAccountTasks() != null) {
				userTaskList = user.getAccountTasks();
			}
			taskDetails.setTaskId(task.getTaskId());
			taskDetails.setTaskStatus(ProjectOnboardingConstant.YET_TO_START);
			taskDetails.setTaskName(accounttaskRequest.getTask().getTaskName());
			taskDetails.setAssignedBy(accounttaskRequest.getUpdatedBy());
			taskDetails.setAssignedTime(LocalDateTime.now());
			taskDetails.setStartTime(LocalDateTime.now());
			taskDetails.setEndTime(LocalDateTime.now().plusDays(accounttaskRequest.getTask().getCompletionDays()));
			userTaskList.add(taskDetails);
			user.setAccountTasks(userTaskList);
			Update userUpdate = new Update();
			userUpdate.set("accountTasks", user.getAccountTasks());
			mongoTemplate.upsert(query, userUpdate, UserEntity.class);
		}
		
		log.info("AccountService saveTasks ends with {}",taskResponse);
		return taskResponse;
	}
	
	/**
	 * Method to update task status
	 * @param saveTaskStatusRequest
	 * @return
	 */
	public List<TaskDetails> saveAccountStatus(AccountTaskStatusRequest saveTaskStatusRequest) //throws Exception
	{
		log.info("Method for saving the account task status");
		Query query = new Query();
				query.addCriteria(new Criteria().andOperator(Criteria.where(ProjectOnboardingConstant.USER_ID).is(saveTaskStatusRequest.getUserId()),
						Criteria.where("accountId").is(saveTaskStatusRequest.getAccountId())));

		UserEntity user = mongoTemplate.findOne(query, UserEntity.class);

		if (!ObjectUtils.isEmpty(user)) {
			
			List<TaskDetails> tasks = user.getAccountTasks();
			for (TaskStatusRequest taskStatusRequest : saveTaskStatusRequest.getTaskStatusRequest()) {

				List<TaskDetails> taskWithGivenTaskId = tasks.stream()
						.filter(s -> s.getTaskId() == taskStatusRequest.getTaskId()).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(taskWithGivenTaskId)) {
					taskWithGivenTaskId.get(0).setTaskStatus(taskStatusRequest.getTaskStatus());
					taskWithGivenTaskId.get(0).setUpdatedBy(saveTaskStatusRequest.getUpdatedBy());
					taskWithGivenTaskId.get(0).setUpdateTime(LocalDateTime.now());
					
					Update update = new Update();
					update.set("accountTasks", tasks);
					mongoTemplate.updateFirst(query, update, UserEntity.class);
				} else {
					log.error("AccountService saveaccountStatus method Task not found");
					throw new ProjectOnboardingException(ProjectOnboardingConstant.TASK_NOT_FOUND);
				}
			}
			return tasks;
		} else {
			log.error("AccountService saveaccountStatus method Id not found");
			throw new ProjectOnboardingException(ProjectOnboardingConstant.PROJECT_NOT_FOUND);
		}

	}
}
