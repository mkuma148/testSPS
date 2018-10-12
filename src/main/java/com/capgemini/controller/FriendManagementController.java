package com.capgemini.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.BaseResponse;
//import com.capgemini.model.BaseResponse;
import com.capgemini.model.CommonFriendsListResponse;
import com.capgemini.model.EmailsListRecievesUpdatesResponse;
import com.capgemini.model.Subscriber;
import com.capgemini.model.UserFriendsListResponse;
import com.capgemini.service.FrientMangmtService;
import com.capgemini.validation.FriendManagementValidation;

/**
 * @author vishwman
 *
 */
@RestController
@Validated
@EntityScan( basePackages = {"com.capgemini.entity"} )
@RequestMapping(value = "/test")
public class FriendManagementController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final String sharedKey = "SHARED_KEY";
	private static final String SUCCESS_STATUS = "Success";
	private static final String ERROR_STATUS = "error";
	private static final int CODE_SUCCESS = 100;
	private static final int AUTH_FAILURE = 102;

	
		public FrientMangmtService frndMngtServc;

		
		FriendManagementValidation fmError;

		@Autowired public FriendManagementController(FrientMangmtService frndMngtServc,FriendManagementValidation fmError) {
			this.frndMngtServc=frndMngtServc;
			this.fmError=fmError;
		}

	/*
	 * http://localhost:8086/friendmgt/test/create
	 * requestobject: {  
	   "{ 
"friends": [  
{"email":"soni.mukesh915@gmail.com"}, 
{"email":"som@gmail.com"}
   	]
}*/

	// need validation---------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<FriendManagementValidation> newFriendConnection(@Valid @RequestBody com.capgemini.model.UserRequest userReq, BindingResult results)throws ResourceNotFoundException {
		LOG.info("newFriendConnection :: ");
		FriendManagementValidation fmResponse = new FriendManagementValidation();
		//BaseResponse baseResponse = new BaseResponse();
		ResponseEntity<FriendManagementValidation> re = null;
		try{
			fmResponse = frndMngtServc.addNewFriendConnection(userReq)	;
			String isNewfrndMangmReqSuccess = fmResponse.getStatus();
					
			//LOG.info("newFriendConnection :: "+isNewfrndMangmReqSuccess);
			
			if(isNewfrndMangmReqSuccess.equalsIgnoreCase("Success")){
				fmResponse.setStatus(SUCCESS_STATUS);
				//response.setCode(CODE_SUCCESS);
				re =  new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.OK);
			} else {
				fmResponse.setStatus(ERROR_STATUS);
				//response.setCode(AUTH_FAILURE);
			}
			re =  new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.OK);

		}catch(Exception e) {
			LOG.error(e.getMessage());
			re =  new ResponseEntity<FriendManagementValidation>(fmResponse, HttpStatus.SERVICE_UNAVAILABLE);

		} 

		return re;


	}



	// Request url - http://localhost:8086/friendmgt/test/friends/sanjib@gmail.com
	/*@RequestMapping(value = "/friends/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserFriendsListResponse> retrieveFriendList(@Email @NonNull @NotBlank @PathVariable String id)throws ResourceNotFoundException {	
		LOG.info("retrieveFriendList :: ");
		UserFriendsListResponse response = frndMngtServc.retrieveFriendsEmails(id );
		ResponseEntity<UserFriendsListResponse> responseEntity = null;
		try{


			if(response.getStatus() == SUCCESS_STATUS){
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.BAD_REQUEST);

			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		} 
		return responseEntity;

	}*/


	// Request url - http://localhost:8086/friendmgt/test/friendlist
	/*request -- {  

	         "email":"abc@gmail.com"

	         }*/
	
	
	/**
	 * 
	 * @param friendListRequest
	 * @param result
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/friendlist", method = RequestMethod.POST)
	public ResponseEntity<UserFriendsListResponse> getFriendList(@Valid @RequestBody com.capgemini.model.FriendListRequest friendListRequest, BindingResult result)throws ResourceNotFoundException {
		LOG.info("--getFriendList :: " +friendListRequest.getEmail());
		UserFriendsListResponse response = frndMngtServc.getFriendList(friendListRequest );
		ResponseEntity<UserFriendsListResponse> responseEntity = null;
		try{
			if(response.getStatus() == SUCCESS_STATUS){
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.BAD_REQUEST);
				//response.setCode(AUTH_FAILURE);
			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<UserFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		} 
		return responseEntity;


	}


	// Request url - http://localhost:8086/friendmgt/test/friends?email1=sanjib@gmail.com&email2=sanjib1@gmail.com
	/*@RequestMapping(value = "/friends", method = RequestMethod.GET)
	public ResponseEntity<CommonFriendsListResponse> retrieveCommonFriendList(@Email @RequestParam(name="email1" , required = true) String email1, @Email @RequestParam(name="email2" , required = true) String email2)throws ResourceNotFoundException {	
		LOG.info("retrieveCommonFriendList");
		ResponseEntity<CommonFriendsListResponse> responseEntity = null;
		CommonFriendsListResponse response = new CommonFriendsListResponse();
		try{
			response = frndMngtServc.RetrieveCommonFriendList(email1,email2 );

			if(response.getStatus() == SUCCESS_STATUS){
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.BAD_REQUEST);
				//response.setCode(AUTH_FAILURE);
			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		} 
		return responseEntity;

	}*/



	// Request url - http://localhost:8086/friendmgt/test/friends
	/*{  
			   "friends":[  
			         {"email":"abc@gmail.com"},
			         {"email":"som@gmail.com"}
			   ]
			}*/
	@RequestMapping(value = "/friends", method = RequestMethod.POST)

	public ResponseEntity<CommonFriendsListResponse> getCommonFriendList(@Valid @RequestBody com.capgemini.model.CommonFriendsListRequest  commonFrndReq) throws ResourceNotFoundException {	
		LOG.info("getCommonFriendList");
		ResponseEntity<CommonFriendsListResponse> responseEntity = null;
		CommonFriendsListResponse response = new CommonFriendsListResponse();
		try{
			response = frndMngtServc.retrieveCommonFriendList(commonFrndReq.getFriends().get(0),commonFrndReq.getFriends().get(1) );


			if(response.getStatus() == SUCCESS_STATUS){
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.BAD_REQUEST);
				//response.setCode(AUTH_FAILURE);
			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<CommonFriendsListResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		} 
		return responseEntity;
	}



	/**
	 * @param subscriber
	 * @param result
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value="/subscribe", method = RequestMethod.PUT)
	public ResponseEntity<FriendManagementValidation> subscribeFriend(@Valid @RequestBody com.capgemini.model.Subscriber subscriber, BindingResult result)throws ResourceNotFoundException {
		LOG.info("subscribeFriend ::");
		//Validation
		if(result.hasErrors()) {
			return handleValidation(result);
		}


		ResponseEntity<FriendManagementValidation> responseEntity = null;
		FriendManagementValidation fmv = new FriendManagementValidation();

		try {
			fmv =frndMngtServc.subscribeTargetFriend(subscriber);
			if(fmv.getStatus() == SUCCESS_STATUS) {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.OK);
			}else {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.SERVICE_UNAVAILABLE);
		}

		return responseEntity;

	}
	
	
	
	
	
	/**
	 * 
	 */
	@RequestMapping(value="/unsubscribe", method = RequestMethod.PUT)
	public ResponseEntity<FriendManagementValidation> unSubscribeFriend(@Valid @RequestBody com.capgemini.model.Subscriber subscriber, BindingResult result)throws ResourceNotFoundException {
		//Validation
		ResponseEntity<FriendManagementValidation> responseEntity = null;
		/*if(result.hasErrors()) {
			return handleValidation(result);
		}*/
		boolean isValid=validateInput(subscriber);
		if(!isValid) {
			responseEntity = new ResponseEntity<FriendManagementValidation>(HttpStatus.BAD_REQUEST);
		}
		
		FriendManagementValidation fmv=null;
		try {
			fmv =frndMngtServc.unSubscribeTargetFriend(subscriber);
			if(fmv.getStatus() == SUCCESS_STATUS) {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.OK);
			}else {
				responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e) {
			responseEntity = new ResponseEntity<FriendManagementValidation>(fmv, HttpStatus.BAD_REQUEST);
		}

		return responseEntity;
	}


	private boolean validateInput(Subscriber subscriber) {
		// TODO Auto-generated method stub
		String requestor=subscriber.getRequestor();
		String target=subscriber.getTarget();
		if(requestor==null || target==null || requestor.equalsIgnoreCase(target)) {
			return false;
		}
		return true;
	}
	
	

	/*http://localhost:8086/friendmgt/test/friends/updatelist
	request :{  

        "sender":"abc@gmail.com",
        "text":"som@gmail.com"

    }*/
	@RequestMapping(value = "/friends/updatelist", method = RequestMethod.PUT)
	public ResponseEntity<EmailsListRecievesUpdatesResponse> emailListRecievesupdates(@Valid @RequestBody com.capgemini.model.EmailsListRecievesUpdatesRequest emailsList, BindingResult result)throws ResourceNotFoundException {

		LOG.info("emailListRecievesupdates ::");

		ResponseEntity<EmailsListRecievesUpdatesResponse> responseEntity = null;
		EmailsListRecievesUpdatesResponse response = new EmailsListRecievesUpdatesResponse();
		try{
			response = frndMngtServc.emailListRecievesupdates(emailsList );
			System.out.println("---------------");
			if(response.getStatus().toString() == SUCCESS_STATUS){
				response.setStatus(SUCCESS_STATUS);
				responseEntity = new ResponseEntity<EmailsListRecievesUpdatesResponse>(response, HttpStatus.OK);
			} else {
				response.setStatus(ERROR_STATUS);
				responseEntity = new ResponseEntity<EmailsListRecievesUpdatesResponse>(response, HttpStatus.BAD_REQUEST);
				//response.setCode(AUTH_FAILURE);
			}
		}catch(Exception e) {
			LOG.error(e.getMessage());
			responseEntity =  new ResponseEntity<EmailsListRecievesUpdatesResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);

		} 
		return responseEntity;

	}




	


	/**
	 * This method is used for client validation
	 * @param result
	 * @return
	 */
	private ResponseEntity<FriendManagementValidation> handleValidation(BindingResult result) {
		fmError.setStatus("Failed");
		if(result.getFieldError("requestor") != null && result.getFieldError("target") != null) {
			fmError.setErrorDescription(result.getFieldError("requestor").getDefaultMessage()+" "+result.getFieldError("target").getDefaultMessage());
		}else if(result.getFieldError("target") != null) {
			fmError.setErrorDescription(result.getFieldError("target").getDefaultMessage());
		}else{
			fmError.setErrorDescription(result.getFieldError("requestor").getDefaultMessage());

		}
		return new ResponseEntity<FriendManagementValidation>(fmError, HttpStatus.BAD_REQUEST);

	}


	/*@RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.GET)
	  public Shipwreck get(@PathVariable Long id){
	    return ShipwreckStub.get(id);
	  }
	  @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.PUT)
	  public Shipwreck update(@PathVariable Long id, @RequestBody Shipwreck shipwreck){
	    return ShipwreckStub.update(id, shipwreck);
	  }
	  @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.DELETE)
	  public Shipwreck delete(@PathVariable Long id){
	    return ShipwreckStub.delete(id);
	  }*/

}
