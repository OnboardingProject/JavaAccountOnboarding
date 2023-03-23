package com.account.onboarding.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AboutCustomerResponse {
	private String customerName;
	private String aboutCustomer;
}