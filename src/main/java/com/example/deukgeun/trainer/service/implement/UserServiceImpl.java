package com.example.deukgeun.trainer.service.implement;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository trainerUserRepository;
	
	public User getList(String keyword) {
		return trainerUserRepository.findByNameOrGroupName(keyword, keyword); 
	}
	
	public Long save(User user) {
		User res = trainerUserRepository.save(user);
		return res.getId();
	}
	
	
}
