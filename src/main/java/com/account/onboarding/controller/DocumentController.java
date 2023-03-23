package com.account.onboarding.controller;

import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.account.onboarding.entity.Account;
import com.account.onboarding.response.FileUploadResponse;
import com.account.onboarding.service.impl.DocumentService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 *   Account Controller class created by swathi
 *
 */
@RestController
@Validated
@Slf4j
public class DocumentController {

	@Autowired
	private DocumentService docService;

// private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);
	/**
	 * @param accountId
	 * @param multipartFile
	 * @param doc
	 * @return
	 * @throws Exception     
	 */
	@RequestMapping(path = "/account/documentId/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Inserted the account details"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "400", description = "Bad Request") })
	public ResponseEntity<?> uploadFile1(@RequestParam(name = "accountId", required = true) String accountId,
			@RequestPart("file") MultipartFile multipartFile) throws Exception {
		FileUploadResponse response;
		log.info("Uploading file to specific acount id :{}", accountId);
		response = docService.uploadAccountFiles(accountId, multipartFile);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(path = "/account/task/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Inserted the account details"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "400", description = "Bad Request") })
	public ResponseEntity<?> uploadAccountTaskFile(@RequestParam(name = "accountId", required = true) String accountId,
			@RequestParam(name = "taskId", required = true) String taskId,
			@RequestPart("file") MultipartFile multipartFile) throws Exception {
		FileUploadResponse response;
		log.info("Uploading file to specific acount id :{}", accountId);
		response = docService.uploadAccountTaskFiles(accountId, taskId, multipartFile);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(path = "/account/documentId/download", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Downloading the account details"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "400", description = "Bad Request") })
	public ResponseEntity<?> downloadFile(@RequestParam(name = "accountId", required = true) String accountId,@RequestParam(name = "fileName", required = true) String fileName) throws Exception {
		FileUploadResponse response;
		log.info("Downloading file to specific acount id :{}", accountId);
		response = docService.downloadAccountFiles(accountId,fileName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/account/task/download", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Downloading the account task details"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error"),
			@ApiResponse(responseCode = "400", description = "Bad Request") })
	public ResponseEntity<?> downloadTaskFile(@RequestParam(name = "accountId", required = true) String accountId,@RequestParam(name = "taskId", required = true) String taskId,@RequestParam(name = "fileName", required = true) String fileName) throws Exception {
		FileUploadResponse response;
		log.info("Downloading file to specific acount id :{}", accountId);
		response = docService.downloadAccountTaskFiles(accountId,taskId,fileName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
