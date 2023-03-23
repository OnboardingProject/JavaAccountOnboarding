package com.account.onboarding.request;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AboutCustomerRequest {

	private String accountId;
	@NotEmpty
	private String aboutCustomer;
	private String updatedBy;
}