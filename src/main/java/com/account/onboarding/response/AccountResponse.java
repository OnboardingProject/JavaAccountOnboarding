package com.account.onboarding.response;

import java.time.LocalDateTime;
import java.util.List;

import com.account.onboarding.entity.AccountInitiative;
import com.account.onboarding.entity.CustomerEngagement;
import com.account.onboarding.entity.Documents;
import com.account.onboarding.entity.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountResponse {
	private String accountId;
	private String accountName;
	private LocalDateTime createdDate;
	private String createdBy;
	private LocalDateTime updatedDate;
	private String updatedBy;
	private String aboutCustomer;
	private List<CustomerEngagement> engagements;
	private String accountOverview;
	private List<AccountInitiative> initiatives;
	private List<Documents> documents;
	private List<Task> tasks;

}
