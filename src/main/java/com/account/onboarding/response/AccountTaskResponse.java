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
public class AccountTaskResponse {

	private String accountId;
	private String accountName;
	private LocalDateTime updatedDate;
	private String updatedBy;
	private List<Task> tasks;
}
