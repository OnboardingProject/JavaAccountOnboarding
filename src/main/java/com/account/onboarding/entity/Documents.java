package com.account.onboarding.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documents {
	@Id
	private String documentId;
	private String documentName;
	private String documentDesc;
	private Integer documentSeq;
	private String documentStatus;
	private String documentPath;

}
