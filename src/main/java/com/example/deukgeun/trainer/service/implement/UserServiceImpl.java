package com.example.deukgeun.trainer.service.implement;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.UserJoinRequest;
import com.example.deukgeun.trainer.response.UserListResponse;
import com.example.deukgeun.trainer.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	public List<UserListResponse> getList(String keyword) {
		return userRepository.findByNameOrGroupName(keyword, keyword); 
	}

	public Long save(User user) {
		User res = userRepository.save(user);
		return res.getId();
	}

	@Transactional(readOnly = true)
	public BindingResult checkEmailDuplication(UserJoinRequest request, BindingResult bindingResult) {
		boolean emailDuplicate = userRepository.existsByEmail(request.getEmail());
		if (emailDuplicate) {
		  bindingResult.addError(new FieldError("email", "email", "이미 존재하는 이메일입니다."));
		}
		
		return bindingResult;
	}
}
