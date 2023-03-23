package com.account.onboarding.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.account.onboarding.entity.Task;

@Repository
public interface TaskRepository extends MongoRepository<Task, Integer>{
	

}
