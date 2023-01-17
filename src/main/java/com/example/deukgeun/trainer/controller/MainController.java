package com.example.deukgeun.trainer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.deukgeun.trainer.response.TrainerUserResponse;
import com.example.deukgeun.trainer.service.MainService;


@RestController("trainer.controller.MainController")
@RequestMapping("/trainer")
public class MainController {
	
	@Autowired
	private MainService mainService;
	
	@RequestMapping("/")
	public ResponseEntity<TrainerUserResponse> list() {
		TrainerUserResponse list = TrainerUserResponse.fromEntity(mainService.getList("김트레이너"));
		
		return ResponseEntity
				.ok()
				.body(list);
	}
	
}
