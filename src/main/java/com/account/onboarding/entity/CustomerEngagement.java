package com.account.onboarding.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomerEngagement {
	@Id
	private String engagementName;
	private String engagementDesc;
}