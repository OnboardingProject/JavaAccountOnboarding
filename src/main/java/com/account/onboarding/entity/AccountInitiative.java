package com.account.onboarding.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInitiative {
	@Id
	private String initiativeName;
	private String initiativeDescription;
}
