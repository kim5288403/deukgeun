package com.example.deukgeun.trainer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.request.UserRequest;
import com.example.deukgeun.trainer.response.UserListResponse;
import com.example.deukgeun.trainer.service.ProfileService;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;

@RestController("trainer.controller.UserController")
@RequestMapping("/trainer")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private ProfileServiceImpl profileService; 
	
	@RequestMapping(method = RequestMethod.GET, path = "/")
	public ResponseEntity<UserListResponse> list(String keyword) {
		User list = userService.getList(keyword);
		UserListResponse result = UserListResponse.fromEntity(list);
		
		return ResponseEntity
				.ok()
				.body(result);
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/")
	@Transactional
	public ResponseEntity<?> save(@RequestBody UserRequest request) {
		try {
			
			User user = UserRequest.create(request);
			Long userId = userService.save(user);
			
			ProfileRequest profileRequest = new ProfileRequest(userId, request.getProfileImage());
			Profile profile = ProfileRequest.create(profileRequest);
			profileService.save(profile);
			
			return ResponseEntity
					.ok()
					.body("회원가입 성공했습니다.");
		} catch(Exception $exception) {
			return ResponseEntity
					.badRequest()
					.body($exception);
		}
	
	}
}

