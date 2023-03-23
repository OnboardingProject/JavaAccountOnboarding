package com.account.onboarding.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.account.onboarding.dao.impl.UserDaoImpl;
import com.account.onboarding.entity.Types;
import com.account.onboarding.entity.UserEntity;


/**
 * {@link UserDaoImpl}
 * @author 
 *
 */
@ExtendWith(MockitoExtension.class)
public class UserDaoImplTest {

	@InjectMocks
	private UserDaoImpl userDaoImpl;

	@Mock
	private MongoTemplate mongoTemplate;
	
	/**
	 * {@link UserDaoImpl#getRoleId(roleId)}
	 * This method checks the test if the roleId is invalid
	 */
	@Test
	public void getRoleIdIfEmptyTest() {
		Integer roleId = 1;
		String permission = "Edit";
		
		Types types = new Types("Role", null, "Admin", permission);
		Query query = new Query();
		Integer role = null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("typeId").is(roleId)), Types.class)).thenReturn(types);
		role = userDaoImpl.getRoleId(roleId);
		assertEquals(role,types.getTypeId() );
	}
	/**
	 * {@link UserDaoImpl#getRoleId(roleId)}
	 * This method checks the test if the roleId given is valid
	 */
	@Test
	public void getRoleIdIfNotEmptyTest() {
		Integer roleId = 2;
		String permission = "Edit";
		
		Types types = new Types("Role", 1, "Admin", permission);
		Query query = new Query();
		Integer role = null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("typeId").is(roleId)), Types.class)).thenReturn(types);
		role = types.getTypeId();
		role = userDaoImpl.getRoleId(roleId);
		assertEquals(role,types.getTypeId());
	}
	
	/**
	 * {@link UserDaoImpl#getRoleId(roleId)}
	 * This method checks the test when query returns empty Types class
	 */
	@Test
	public void getRoleIdIfTypesEmptyTest() {
		Integer roleId = 1;
		String permission = "Edit";
		
		Types types = new Types("Role", null, "Admin", permission);
		Query query = new Query();
		Integer role = null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("typeId").is(roleId)), Types.class)).thenReturn(null);
		role = userDaoImpl.getRoleId(roleId);
		assertEquals(role,types.getTypeId() );
	}
	
	/**
	 * {@link UserDaoImpl#getUser(accountName, createdBy)}
	 * This method checks the test when the method returns null
	 * 
	 */
	@Test
	public void getHierarchyIfEmptyTest() {
		UserEntity user = new UserEntity("U2345", "Kiara",null, "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
	new Date(), "U2345", "U2345", new Date(), 1,"", null,"", null, null);
		Query query = new Query();
		String accountId="abc";
		String createdBy="U2345";
		String hierarchy=null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("accountId").is(accountId).and("userId").is(createdBy)),UserEntity.class)).thenReturn(user);
		hierarchy = userDaoImpl.getUser(accountId,createdBy);
		assertEquals(hierarchy,user.getHierarchy());
	}
	/**
	 * {@link UserDaoImpl#getUser(accountName, createdBy)}
	 * This method tests when the query returns empty user
	 * 
	 */
	@Test
	public void getHierarchyIfUserEmptyTest() {
		UserEntity user = new UserEntity("U2345", "Kiara",null, "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
	new Date(), "U2345", "U2345", new Date(), 1,"", null,"", null, null);
		Query query = new Query();
		String accountId="abc";
		String createdBy="U2345";
		String hierarchy=null;
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("accountId").is(accountId).and("userId").is(createdBy)),UserEntity.class)).thenReturn(null);
		hierarchy = userDaoImpl.getUser(accountId,createdBy);
		//assertEquals(hierarchy,user.getHierarchy());
	}
	/**
	 * {@link UserDaoImpl#getUser(accountName, createdBy)}
	 * This is the test case if the method returns hierarchy
	 * 
	 */
	@Test
	public void getHierarchyIfNotEmptyTest() {
		UserEntity user = new UserEntity("U2345", "Kiara",null, "abc", "Kiara", "Kevin", "kiara@abc.com", "9898988787", "java",
	new Date(), "U2345", "U2345", new Date(), 1,"", null,"", null, null);
		Query query = new Query();
		String accountId="abc";
		String createdBy="U2345";
		String hierarchy=user.getHierarchy();
		when(mongoTemplate.findOne(query.addCriteria(Criteria.where("accountId").is(accountId).and("userId").is(createdBy)),UserEntity.class)).thenReturn(user);
		hierarchy = userDaoImpl.getUser(accountId,createdBy);
		assertEquals(hierarchy,user.getHierarchy());
	}
}