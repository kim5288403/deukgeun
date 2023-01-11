package com.example.deukgeun.trainer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.deukgeun.trainer.service.MainService;
import com.example.deukgeun.trainer.service.implement.MainServiceImpl;

@RestController("trainer.controller.MainController")
@RequestMapping("/trainer")
public class MainController {
	
	@Autowired
	private MainService mainService;
	
	@RequestMapping("/")
	public ResponseEntity<?> list() {
		ResponseEntity<?> list = mainService.getList("gd");
		return list;
	}
	
}
