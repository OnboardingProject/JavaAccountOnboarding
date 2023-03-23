package com.account.onboarding.request;

import com.account.onboarding.entity.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTaskRequest {
	private String accountId;
	private Task task;
	private String updatedBy;
}
