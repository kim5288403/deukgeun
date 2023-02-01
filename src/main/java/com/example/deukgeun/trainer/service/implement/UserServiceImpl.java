package com.example.deukgeun.trainer.service.implement;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.UserJoinRequest;
import com.example.deukgeun.trainer.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	public User getList(String keyword) {
		return userRepository.findByNameOrGroupName(keyword, keyword); 
	}

	public Long save(User user) {
		User res = userRepository.save(user);
		return res.getId();
	}

	public Map<String, String> validateHandling(BindingResult bindingResult) {
		Map<String, String> validatorResult = new HashMap<>();

		/* 유효성 검사에 실패한 필드 목록을 받음 */
		for (FieldError error : bindingResult.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}

	@Transactional(readOnly = true)
	public void checkEmailDuplication(UserJoinRequest request) {
		boolean emailDuplicate = userRepository.existsByEmail(request.getEmail());
		if (emailDuplicate) {
			throw new IllegalStateException("이미 존재하는 이메일입니다.");
		}
	}
}
