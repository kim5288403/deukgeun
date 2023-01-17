package com.example.deukgeun.trainer.service.implement;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.deukgeun.trainer.entity.TrainerUser;
import com.example.deukgeun.trainer.repository.TrainerUserRepository;
import com.example.deukgeun.trainer.service.MainService;

@Service
public class MainServiceImpl implements MainService{
	
	@Autowired
	private TrainerUserRepository trainerUserRepository;
	
	@Override
	public TrainerUser getList(String keyword) {
		return trainerUserRepository.findByNameOrGroupName(keyword, keyword); 
	}
}
