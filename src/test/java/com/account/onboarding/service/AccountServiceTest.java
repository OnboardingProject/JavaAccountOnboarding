package com.account.onboarding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.account.onboarding.entity.Account;
import com.account.onboarding.entity.CustomerEngagement;
import com.account.onboarding.exception.AccountCustomerException;
import com.account.onboarding.repository.AccountRepository;
import com.account.onboarding.request.AboutCustomerRequest;
import com.account.onboarding.request.AccountRequest;
import com.account.onboarding.request.CustomerEngagementRequest;
import com.account.onboarding.response.AboutCustomerResponse;
import com.account.onboarding.response.AccountResponse;
import com.account.onboarding.service.impl.AccountServiceImpl;



@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private ModelMapper modelMapper;

	@Test
	public void testCreateAccount_Success() {
		Account account1 = new Account("accountid1", "test1", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				"testCreatedBy1",null,"", "", null, "", null, null,null);
		AccountRequest account = new AccountRequest("accountid1", "test1", "testCreatedBy",null,null,"",null,null,null);
		when(accountRepository.save(any())).thenReturn(account1);
		AccountResponse savedAccount = accountServiceImpl.createAccount(account);
		
		
	}

	@Test
	public void testCreateAccount_NullPointerException() {
		try {
			AccountRequest accountvo = null;
			accountServiceImpl.createAccount(accountvo);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testAddAboutCustomer_Success() {
		AboutCustomerRequest aboutCustomerRequestVO = new AboutCustomerRequest("accountid", "testDescription",
				"testUpdatedBy");
		Optional<Account> account = Optional.of(new Account("accountid", "customerNameTest",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "testCreatedBy", null,"", "", null, "", null, null,null));
		when(accountRepository.findById(any())).thenReturn(account);
		when(accountRepository.save(any())).thenReturn(account.get());

		AboutCustomerResponse aboutCustomerResponseVO = accountServiceImpl.addAboutCustomer(aboutCustomerRequestVO);
		assertEquals("testDescription", aboutCustomerResponseVO.getAboutCustomer());
		//assertEquals("customerNameTest", aboutCustomerResponseVO.getCustomerName());
	}

	@Test
	public void testAddAboutCustomer_DataPresentException() {
		try {
			AboutCustomerRequest aboutCustomerRequestVO = new AboutCustomerRequest("accountid", "testDescription",
					"testUpdatedBy");
			Optional<Account> account = Optional
					.of(new Account("accountid", "customerNameTest", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
							"testCreatedBy", null, "", "aboutCustomer", null, "", null, null,null));
			when(accountRepository.findById(any())).thenReturn(account);
			accountServiceImpl.addAboutCustomer(aboutCustomerRequestVO);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testAddAboutCustomer_IdNotFoundException() {
		try {
			AboutCustomerRequest aboutCustomerRequestVO = new AboutCustomerRequest("id", "testDescription",
					"testUpdatedBy");
			accountServiceImpl.addAboutCustomer(aboutCustomerRequestVO);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testAddAboutCustomer_NullPointerException() {
		try {
			AboutCustomerRequest aboutCustomerRequestVO = null;
			accountServiceImpl.addAboutCustomer(aboutCustomerRequestVO);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testAddCustomerEngagement_NullValues() {
		try {
			CustomerEngagementRequest customerEngagementRequestVO = null;
			accountServiceImpl.addCustomerEngagements(customerEngagementRequestVO);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testAddCustomerEngagement_IdNotFoundException() {
		try {
			CustomerEngagementRequest customerEngagementRequestVO = new CustomerEngagementRequest("id",
					"testEngagementName", "testDescription", "testUpdatedBy");
			accountServiceImpl.addCustomerEngagements(customerEngagementRequestVO);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testAddCustomerEngagement_ExistingEngagementNameException() {
		CustomerEngagement customerEngagement = new CustomerEngagement("testname", "testdesc");
		List<CustomerEngagement> engList = new ArrayList<CustomerEngagement>();
		engList.add(customerEngagement);
		Optional<Account> account1 = Optional.of(new Account("accountid1", "test1",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "testCreatedBy1", null, "", "", engList, "", null, null,null));
		when(accountRepository.findById(any())).thenReturn(account1);
		try {
			CustomerEngagementRequest customerEngagementRequestVO = new CustomerEngagementRequest("testId",
					"testname", "testDescription", "testUpdatedBy");
			accountServiceImpl.addCustomerEngagements(customerEngagementRequestVO);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testAddCustomerEngagement_Success() {

		CustomerEngagement customerEngagement = new CustomerEngagement("testname1", "testdesc1");
		List<CustomerEngagement> engList = new ArrayList<CustomerEngagement>();
		engList.add(customerEngagement);
		Optional<Account> account1 = Optional.of(new Account("accountid1", "test1",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "testCreatedBy1", null, "", "", engList, "", null, null,null));
		when(accountRepository.findById(any())).thenReturn(account1);

		CustomerEngagementRequest customerEngagementRequestVO = new CustomerEngagementRequest("accountid1",
				"testEngName", "testDescription", "testUpdatedBy");
		when(accountRepository.save(any())).thenReturn(account1.get());

		List<CustomerEngagement> list = accountServiceImpl.addCustomerEngagements(customerEngagementRequestVO);
		assertEquals(2, list.size());
		assertEquals("testdesc1", list.get(0).getEngagementDesc());
		assertEquals("testEngName", list.get(1).getEngagementName());
	}

	@Test
	public void testgetAllAccounts_success() {
		Account account1 = new Account("accountid1", "test1", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				"testCreatedBy1", null, "", "", null, "", null, null,null);
		Account account2 = new Account("accountid2", "test2", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				"testCreatedBy", null, "", "", null, "", null, null,null);
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(account1);
		accounts.add(account2);
		when(accountRepository.findAll()).thenReturn(accounts);
		List<Account> accountList = accountServiceImpl.getAllAccounts();
		assertEquals(accounts, accountList);
		assertEquals(2, accountList.size());
		assertEquals("testCreatedBy1", accountList.get(0).getCreatedBy());
	}

	@Test
	public void testgetAllAccounts_failed() {
		try {
			when(accountRepository.findAll()).thenThrow(AccountCustomerException.class);
			accountServiceImpl.getAllAccounts();
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testUpdateCustomer_Success() {
		AboutCustomerRequest aboutCustomerRequestVO = new AboutCustomerRequest("accountid", "testDescription",
				"testUpdatedBy");
		Account account1 = new Account("accountid1", "test1", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
				"testCreatedBy1", null, "", "", null, "", null, null,null);
		account1.setAboutCustomer(aboutCustomerRequestVO.getAboutCustomer());
		account1.setUpdatedBy(aboutCustomerRequestVO.getUpdatedBy());
		account1.setUpdatedDate(LocalDateTime.now());
		Optional<Account> account = Optional.of(new Account("accountid", "customerNameTest",
				LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "testCreatedBy", null, "", "", null, "", null, null,null));
		when(accountRepository.findById(any())).thenReturn(account);
		when(accountRepository.save(any())).thenReturn(account.get());
		AboutCustomerResponse updatedAccount = accountServiceImpl.updateAboutCustomer(aboutCustomerRequestVO);
		assertEquals("testDescription", updatedAccount.getAboutCustomer());
	}

	@Test
	public void testUpdateCustomer_Failure() {
		try {
			Account account = new Account("accountid1", "test1", LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9),
					"testCreatedBy1", null, "", "", null, "", null, null,null);
			AboutCustomerRequest aboutCustomerRequestVO = new AboutCustomerRequest("accountid", null,
					"testUpdatedBy");
			account.setAboutCustomer(aboutCustomerRequestVO.getAboutCustomer());
			account.setUpdatedBy(aboutCustomerRequestVO.getUpdatedBy());
			account.setUpdatedDate(LocalDateTime.now());
			Optional<Account> accounts = Optional.of(new Account("accountid", null,
					LocalDateTime.of(2020, 3, 19, 1, 0, 8, 9), "testCreatedBy", null, "", "", null, "", null, null,null));
			when(accountRepository.findById(any())).thenReturn(accounts);
			when(accountRepository.save(any())).thenReturn(accounts.get());
			accountServiceImpl.updateAboutCustomer(aboutCustomerRequestVO);
			
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

	@Test
	public void testUpdateCustomer_IdNotFoundException() {
		try {
			AboutCustomerRequest aboutCustomerRequestVO = new AboutCustomerRequest("id", "testDescription",
					"testUpdatedBy");

			accountServiceImpl.updateAboutCustomer(aboutCustomerRequestVO);
		} catch (Exception e) {
			assertTrue(e instanceof AccountCustomerException);
		}
	}

}
