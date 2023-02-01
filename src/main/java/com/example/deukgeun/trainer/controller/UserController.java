package com.example.deukgeun.trainer.controller;


import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.Message;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.request.UserJoinRequest;
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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

	@RequestMapping(method = RequestMethod.POST, path = "/join")
	public ResponseEntity<?> save(
			@RequestPart
			@Valid
			UserJoinRequest request,
			BindingResult bindingResult,
			@RequestPart
			MultipartFile profile
			) {
		
		bindingResult = profileService.validator(profile, bindingResult);
		
		if (bindingResult.hasErrors()) {
			/* 유효성 통과 못한 필드와 메시지를 핸들링 */
			Map<String, String> validatorResult = userService.validateHandling(bindingResult);
			
			return ResponseEntity
					.ok()
					.body(
							Message.builder()
							.data(validatorResult)
							.message("회원 가입 실패 했습니다.")
							.status(StatusEnum.BAD_REQUEST)
							.build()
							);
		}
		
		//구현할 내용
		//1. 코드 정리
		//2. enum custom validator // ok
		//3. file custom validator // ok
		//4. 비밀번호 암호화 저장
		//4-1. 비밀번호 비밀번호 확인 validate
		//5. file save
		//6. front 연동
		//7. 코드 스타일
		
		try {
			userService.checkEmailDuplication(request);
			User user = UserJoinRequest.create(request, passwordEncoder);
			Long userId = userService.save(user);

			//			ProfileRequest profileRequest = ProfileRequest.builder().
			//					trainerUserId(userId).
			//					path(request.getProfileImage()).
			//					build();
			//			
			//			Profile profile = ProfileRequest.create(profileRequest);
			//			profileService.save(profile);

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

