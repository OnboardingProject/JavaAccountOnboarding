package com.account.onboarding.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.account.onboarding.entity.Account;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

	List<Account> findByAccountNameContaining(String accountName);

}