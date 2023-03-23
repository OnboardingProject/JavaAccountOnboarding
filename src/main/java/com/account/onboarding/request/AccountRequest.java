package com.account.onboarding.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import com.account.onboarding.entity.AccountInitiative;
import com.account.onboarding.entity.CustomerEngagement;
import com.account.onboarding.entity.Documents;
import com.account.onboarding.entity.Task;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountRequest {

	@Id
	private String accountId;
	@NotEmpty
	@Size(min = 4, message = "Customer name must be minimum of 4 characters")
	private String customerName;
	private String createdBy;
	private String aboutCustomer;
	private List<CustomerEngagement> engagements;
	private String accountOverview;
	private List<AccountInitiative> initiatives;
	private List<Documents> documents;
	private List<Task> tasks;
	

}