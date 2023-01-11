package com.example.deukgeun.trainer.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.deukgeun.trainer.repository.TrainerUserRepository;
import com.example.deukgeun.trainer.service.MainService;

@Service
public class MainServiceImpl implements MainService{
	
	@Autowired
	private TrainerUserRepository trainerUserRepository;
	
	@Override
	public ResponseEntity<?> getList(String keyword) {
		return (ResponseEntity<?>) trainerUserRepository.findAll();
	}
}
