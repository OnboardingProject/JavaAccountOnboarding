package com.account.onboarding.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTaskStatusRequest {
	private String accountId;
	private String userId;
	private List<TaskStatusRequest> taskStatusRequest;
	private String updatedBy;
	private String updatedOn;

}
