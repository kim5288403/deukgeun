package com.example.deukgeun.trainer.service.implement;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.deukgeun.trainer.repository.TrainerUserRepository;
import com.example.deukgeun.trainer.service.MainService;

@Service
public class MainServiceImpl implements MainService{
	
	@Autowired
	private TrainerUserRepository trainerUserRepository;
	
	@Override
	public ArrayList<?> getList(String keyword) {
		
		return (ArrayList<?>) trainerUserRepository.findAll(); 
	}
}
