package com.example.deukgeun.trainer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.deukgeun.commom.response.Message;
import com.example.deukgeun.commom.response.StatusEnum;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.request.UserRequest;
import com.example.deukgeun.trainer.response.UserListResponse;
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
	public ResponseEntity<?> list(String keyword) {
		try {
			User list = userService.getList(keyword);
			UserListResponse result = UserListResponse.fromEntity(list);
			
			return ResponseEntity
					.ok()
					.body(
							Message.builder()
							.data(result)
							.message("트레이너 조회 성공 했습니다.")
							.status(StatusEnum.OK)
							.build()
							);
			
		} catch (Exception e) {
			return ResponseEntity
					.badRequest()
					.body(
							Message.builder()
							.data(keyword)
							.message(e.getMessage())
							.status(StatusEnum.BAD_REQUEST)
							.build()
							);
		}
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, path = "/")
	public ResponseEntity<?> save(@RequestBody UserRequest request) {
		try {
			User user = UserRequest.create(request);
			Long userId = userService.save(user);

			ProfileRequest profileRequest = ProfileRequest.builder().
					trainerUserId(userId).
					path(request.getProfileImage()).
					build();
			
			Profile profile = ProfileRequest.create(profileRequest);
			profileService.save(profile);
			
			return ResponseEntity
					.ok()
					.body(
							Message.builder()
							.data(user)
							.message("회원 가입 성공 했습니다.")
							.status(StatusEnum.OK)
							.build()
							);
			
		} catch(Exception e) {
			return ResponseEntity
					.badRequest()
					.body(
							Message.builder()
							.data(request)
							.message(e.getMessage())
							.status(StatusEnum.BAD_REQUEST)
							.build()
							);
		}
	}
	
}

