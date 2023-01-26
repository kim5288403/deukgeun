package com.example.deukgeun.trainer.controller;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	public ResponseEntity<UserListResponse> list(String keyword) {
		User list = userService.getList(keyword);
		UserListResponse result = UserListResponse.fromEntity(list);

		return ResponseEntity
				.ok()
				.body(result);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/")
	@Transactional
	public ResponseEntity<Message> save(@RequestBody UserRequest request) {
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		
		try {
			User user = UserRequest.create(request);
			Long userId = userService.save(user);

			ProfileRequest profileRequest = ProfileRequest.builder().
					trainerUserId(userId).
					path(request.getProfileImage()).
					build();
			
			Profile profile = ProfileRequest.create(profileRequest);
			profileService.save(profile);
			
			Message message = Message.builder()
					.data(user)
					.message("회원 가입 성공 했습니다.")
					.status(StatusEnum.OK)
					.build();

			return new ResponseEntity<>(message, headers, HttpStatus.OK);
		} catch(Exception $exception) {
			Message message = Message.builder()
					.data(request)
					.message("회원 가입 실패 했습니다.")
					.status(StatusEnum.BAD_REQUEST)
					.build();
			
			return new ResponseEntity<>(message, headers, HttpStatus.BAD_REQUEST);
		}

	}
}

