package com.capgemini.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import java.util.List;

import org.junit.Before;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.validation.BindingResult;

import com.capgemini.exceptionhandling.ResourceNotFoundException;

import com.capgemini.model.CommonFriendsListRequest;

import com.capgemini.model.CommonFriendsListResponse;

import com.capgemini.model.FriendListRequest;

import com.capgemini.model.Subscriber;

import com.capgemini.model.UserFriendsListResponse;

import com.capgemini.model.UserRequest;

import com.capgemini.repository.FriendMangmtRepo;

import com.capgemini.service.FrientMangmtService;

import com.capgemini.validation.FriendManagementValidation;

@RunWith(SpringRunner.class)

@SpringBootTest

public class FriendManagementControllerTest {

//@Mock

FriendManagementController friendManagementController;

private Subscriber subscriber;

private UserRequest userRequest;

private FriendListRequest friendListRequest;

private CommonFriendsListRequest commonFriendsListRequest;

//@InjectMocks

@Mock

private BindingResult result;

//@Mock

FriendManagementValidation fmError;

@Mock

JdbcTemplate jdbcTemplate;

//@InjectMocks

FriendMangmtRepo friendMangmtRepo;

//@InjectMocks

FrientMangmtService frndMngtServc;

@Mock

BindingResult bindingResult;

@Before

public void setUp() throws Exception {

subscriber=new Subscriber();

userRequest=new UserRequest();

fmError=new FriendManagementValidation();

friendMangmtRepo=new FriendMangmtRepo(fmError,jdbcTemplate);

frndMngtServc=new FrientMangmtService(friendMangmtRepo);

friendManagementController=new FriendManagementController(frndMngtServc,fmError);

friendListRequest=new FriendListRequest();

commonFriendsListRequest=new CommonFriendsListRequest();

}

@Test

public void test_null_requestor_unsubscribe() throws ResourceNotFoundException {

subscriber.setTarget("ravi@gmail.com");

subscriber.setRequestor(null);

when(this.result.hasErrors()).thenReturn(false);

ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber, result);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);

}

@Test

public void test_null_subscriber_unsubscribe() throws ResourceNotFoundException {

when(this.result.hasErrors()).thenReturn(false);

subscriber.setRequestor("ravi@gmail.com");

subscriber.setTarget(null);

ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber, result);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);

}

@Test

public void test_samesubreq_unsubscribe() throws ResourceNotFoundException {

subscriber.setRequestor("ravi@gmail.com");

subscriber.setTarget("ravi@gmail.com");

when(this.result.hasErrors()).thenReturn(false);

ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber, result);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);

}

@Test

public void test_unsubscribe_success() throws ResourceNotFoundException {

subscriber.setRequestor("ravi@gmail.com");

subscriber.setTarget("arvi@gmail.com");

when(this.result.hasErrors()).thenReturn(false);

List<Object> obj=new ArrayList<Object>();

String subscribers="ravi@gmail.com,arvi@gmail.com";

when(this.jdbcTemplate.queryForObject("",obj.toArray(),String.class)).thenReturn(subscribers);

ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber,result);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);

}

//@Test

public void test_unSubscribe_success() throws ResourceNotFoundException {

subscriber.setRequestor("ravi@gmail.com");

subscriber.setTarget("arvi@gmail.com");

when(this.result.hasErrors()).thenReturn(false);

List<Object> obj=new ArrayList<Object>();

//String subscribers="ravi@gmail.com,arvi@gmail.com";

//when(this.jdbcTemplate.queryForObject("",obj.toArray(),String.class)).thenReturn(subscribers);

ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber,result);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

}

@Test

public void test_addFriend_success() throws ResourceNotFoundException {

fmError.setStatus("success");

userRequest.setRequestor("raga@gmail.com");

userRequest.setTarget("raju@gmail.com");

ResponseEntity<FriendManagementValidation> responseEntity=null;

responseEntity=friendManagementController.newFriendConnection(userRequest,bindingResult);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

}

//@Test

public void test_getFriendList_success() throws ResourceNotFoundException {

friendListRequest.setEmail("ranga@gmail.com");

ResponseEntity<UserFriendsListResponse> responseEntity=friendManagementController.getFriendList(friendListRequest,bindingResult);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

}

//@Test

public void test_getCommonFriendList_success() throws ResourceNotFoundException {

List<String> friends=new ArrayList<String>();

friends.add("ranga@gmail.com");

friends.add("ranga1@gmail.com");

commonFriendsListRequest.setFriends(friends);

ResponseEntity<CommonFriendsListResponse> responseEntity=friendManagementController.getCommonFriendList(commonFriendsListRequest);

assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

}

}