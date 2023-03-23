package com.account.onboarding.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.onboarding.constants.ProjectOnboardingConstant;
import com.account.onboarding.exception.ProjectOnboardingException;
import com.account.onboarding.response.ResponsePayLoad;
import com.account.onboarding.response.StatusReportResponse;
import com.account.onboarding.response.TaskPercentageReportResponse;
import com.account.onboarding.service.impl.OnboardingStatusService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Controller for OnboardingStatus module
 * @date : 10 August 2022
 */
@Slf4j
@RestController
@RequestMapping(ProjectOnboardingConstant.ONBOARDING_STATUS)
public class OnboardingStatusController {

	@Autowired
	private OnboardingStatusService onboardingStatusService;

	/**
	 * @param projectId, userId
	 * @return ResponseEntity<ResponsePayLoad>, ProjectOnboardingException,
	 *         Exception
	 * @description : Preview status report of a particular user for a project
	 */
	@GetMapping(ProjectOnboardingConstant.GET_PREVIEW_REPORT)
	@Operation(summary = ProjectOnboardingConstant.GET_PREVIEW_REPORT_SUMMARY, description = ProjectOnboardingConstant.GET_PREVIEW_REPORT_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> getPreviewStatusReport(
			@Valid @PathVariable(ProjectOnboardingConstant.PROJECT_ID) @NotNull @Size(min = 1, message = ProjectOnboardingConstant.INVALID_PROJECT_ID) String projectId,
			@PathVariable(ProjectOnboardingConstant.USER_ID) @NotNull @Size(min = 1, message = ProjectOnboardingConstant.INVALID_USER_ID) String userId) {
		log.info("OnboardingStatusController preview status report starts with projectId {}", projectId);

		TaskPercentageReportResponse taskPercentageReport = onboardingStatusService.getPreviewStatusReport(projectId,
				userId);
		List<Object> statusReportObject = new ArrayList<Object>();
		statusReportObject.add(taskPercentageReport);

		log.info("OnboardingStatusController Status report is returned successfully");
		return new ResponseEntity<ResponsePayLoad>(
				new ResponsePayLoad(statusReportObject, ProjectOnboardingConstant.API_GET_PREVIEW_REPORT_SUCCESS, ""),
				HttpStatus.OK);

	}

	/**
	 * @param projectId, userId
	 * @return ResponseEntity<ResponsePayLoad>, ProjectOnboardingException,
	 *         Exception
	 * @throws Exception
	 * @description : Download status report in excel format for a particular user
	 *              of a project
	 */
	@GetMapping(ProjectOnboardingConstant.EXPORT_REPORT)
	@Operation(summary = ProjectOnboardingConstant.EXPORT_REPORT_SUMMARY, description = ProjectOnboardingConstant.EXPORT_REPORT_DESCRIPTION)
	public ResponseEntity<ResponsePayLoad> exportStatusReportInExcelFormat(
			@Valid @PathVariable(ProjectOnboardingConstant.PROJECT_ID) @Size(min = 1, message = ProjectOnboardingConstant.INVALID_PROJECT_ID) String projectId,
			@PathVariable(ProjectOnboardingConstant.USER_ID) @Size(min = 1, message = ProjectOnboardingConstant.INVALID_USER_ID) String userId)
			throws Exception {
		log.info("In export status report controller");

		StatusReportResponse statusReport = onboardingStatusService.exportStatusReportInExcelFormat(projectId, userId);
		List<Object> statusReportObject = new ArrayList<Object>();
		statusReportObject.add(statusReport);

		log.info("Status report in excel format is downloaded successfully in controller");
		return new ResponseEntity<ResponsePayLoad>(
				new ResponsePayLoad(statusReportObject,
						ProjectOnboardingConstant.API_EXPORT_REPORT_SUCCESS
								+ ProjectOnboardingConstant.getFileNameForExcelReport(projectId, userId),
						""),
				HttpStatus.OK);

	}
}
