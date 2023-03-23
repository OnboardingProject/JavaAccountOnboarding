package com.account.onboarding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.account.onboarding.entity.UserEntity;

/**
 * @author Vanisha Kulsu Mooppen
 * @description : Repository class for accessing User entity.
 * @date : 08 August 2022
 */

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
	UserEntity findByUserId(String id);

	@Query(value = "{$and : [ { userId : ?0 },{roleId : ?1}]}", fields = "{ firstName : 1}")
	String findByRoleAndUserId(String userId, String roleId);

	List<UserEntity> findByRoleId(List<String> roleId);
	
	Optional<UserEntity> findByUserName(String userName);
}