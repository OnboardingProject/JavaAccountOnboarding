package com.account.onboarding.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiativesRequest {
	private String accountId;
	private String initiativeName;
	private String initiativeDescription;
	private String updatedBy;
}