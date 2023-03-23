package com.account.onboarding.response;

import java.util.Date;
import java.util.List;

import com.account.onboarding.entity.AccountDocument;
import com.account.onboarding.entity.ProjectTaskDetails;
import com.account.onboarding.entity.TaskDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
	
	private String userId;
    private String userName;
	private String accountId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNo;
	private String designation;
	private Date createdTime;
	private String createdBy;
	private String lastUpdateBy;
	private Date lastUpdateTime;
	private Integer roleId;
	private String portfolio;
	private List<AccountDocument> accountDocuments;
	private String hierarchy;
	private List<ProjectTaskDetails> projectTasks;
	private List<TaskDetails> accountTasks;

}
