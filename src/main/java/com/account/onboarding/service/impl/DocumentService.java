package com.account.onboarding.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.account.onboarding.entity.Account;
import com.account.onboarding.entity.Documents;
import com.account.onboarding.entity.Task;
import com.account.onboarding.repository.AccountRepository;
import com.account.onboarding.response.FileUploadResponse;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import lombok.extern.slf4j.Slf4j;

/**
 *   Account Service class created by swathi
 *
 */
@Service
@Slf4j
public class DocumentService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private CloudBlobContainer container;

	/**
	 *      *       * @param accountId      * @param multipartFile      * @return
	 *      * @throws Exception     
	 */
	public FileUploadResponse uploadAccountFiles(String accountId, MultipartFile multipartFile) throws Exception {
		FileUploadResponse response = new FileUploadResponse();
		String blobPath = new StringBuilder("ACCOUNT").append("/").append(accountId).append("/")
				.append(multipartFile.getOriginalFilename()).toString();
		uploadToBlob(multipartFile, blobPath);
		try {
			Optional<Account> accounts = accountRepository.findById(accountId);
			if (accounts.isPresent()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				log.info("Creating new folder with accountId:{}", accountId);
				Account account = accounts.get();
				List<Documents> documentList = account.getDocuments();
				int sequence = 1;
				if (!CollectionUtils.isEmpty(documentList)) {
					sequence = (documentList.get(documentList.size() - 1).getDocumentSeq()) + 1;
				}
				Documents documents = new Documents();
				documents.setDocumentName(fileName);
				documents.setDocumentId(sequence + "");
				documents.setDocumentSeq(sequence);
				documents.setDocumentStatus("ACTIVE");
				documentList.add(documents);
				account.setDocuments(documentList);
				accountRepository.save(account);
				log.info("Account entity is updated with document details");
				response.setFileName(fileName);
			} else {
				throw new Exception("Invalid AccountId ");
			}
		} catch (IOException e) {
			throw new IOException("Error saving upload File" + multipartFile.getOriginalFilename(), e);
		}
		return response;
	}

	/**
	 * @param accountId         
	 * @param multipartFile
	 * @return
	 * @throws Exception     
	 */
	public FileUploadResponse uploadAccountTaskFiles(String accountId, String taskId, MultipartFile multipartFile)
			throws Exception {
		FileUploadResponse response = new FileUploadResponse();
		String blobPath = new StringBuilder("ACCOUNT").append("/").append(accountId).append("/").append("TASK")
				.append("/").append(taskId).append("/").append(multipartFile.getOriginalFilename()).toString();
		uploadToBlob(multipartFile, blobPath);
		try {
			Optional<Account> accounts = accountRepository.findById(accountId);
			if (accounts.isPresent()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				log.info("Creating new folder with accountId:{}", accountId);
				Account account = accounts.get();
				Task taskData = account.getTasks().stream().filter(x -> x.getTaskId().equals(taskId)).findFirst()
						.orElseThrow();
				List<Documents> documentList = taskData.getTaskDocuments();
				int sequence = 1;
				if (!CollectionUtils.isEmpty(documentList)) {
					sequence = (documentList.get(documentList.size() - 1).getDocumentSeq()) + 1;
				}
				Documents documents = new Documents();
				documents.setDocumentName(fileName);
				documents.setDocumentId(sequence + "");
				documents.setDocumentSeq(sequence);
				documents.setDocumentStatus("ACTIVE");
				documents.setDocumentPath(blobPath);
				documentList.add(documents);
				taskData.setTaskDocuments(documentList);
				List<Task> tasks = account.getTasks().stream().filter(x -> !x.getTaskId().equals(taskId))
						.collect(Collectors.toList());
				tasks.add(taskData);
				account.setTasks(tasks);
				accountRepository.save(account);
				log.info("Account entity is updated with document details");
				response.setFileName(fileName);
			} else {
				throw new Exception("Invalid AccountId ");
			}
		} catch (IOException e) {
			throw new IOException("Error saving upload File" + multipartFile.getOriginalFilename(), e);
		}
		return response;
	}

	public URI uploadToBlob(MultipartFile multipartFile, String blobPath) throws Exception {

		CloudBlockBlob blob = container.getBlockBlobReference(blobPath);
		blob.upload(multipartFile.getInputStream(), -1);
		URI uri = blob.getUri();
		System.out.println("Blob URL" + uri);
		return uri;
	}

	public FileUploadResponse downloadAccountFiles(String accountId, String fileName)
			throws URISyntaxException, StorageException, IOException {
		FileUploadResponse response = new FileUploadResponse();

		String blobPath = new StringBuilder("ACCOUNT").append("/").append(accountId).append("/").append(fileName)
				.toString();
		downloadToBlob(blobPath, fileName);
		response.setFileName(fileName);

		return response;
	}

	private void downloadToBlob(String blobPath, String fileName)
			throws URISyntaxException, StorageException, IOException {
		OutputStream outputStream = null;
		CloudBlockBlob blob = container.getBlockBlobReference(blobPath);
	//	String localPath = "D:\\Status\\data.docx";
		String localPath = "D:\\Status\\";
		String file=fileName;
	//	blob.download(outputStream);
	//	URI uri = blob.getUri();
	//	CloudBlockBlob blobs = new CloudBlockBlob(new URI(blobPath));
		blob.downloadToFile(localPath+file);
		//blobs.downloadToFile(localPathTry + localFileName);

	}

	public FileUploadResponse downloadAccountTaskFiles(String accountId, String taskId, String fileName)
			throws URISyntaxException, StorageException, IOException {
		FileUploadResponse response = new FileUploadResponse();
		String blobPath = new StringBuilder("ACCOUNT").append("/").append(accountId).append("/").append("TASK")
				.append("/").append(taskId).append("/").append(fileName).toString();
		downloadToBlob(blobPath, fileName);
		response.setFileName(fileName);
		return response;
	}

}