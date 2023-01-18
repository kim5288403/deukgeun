package com.example.deukgeun.trainer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("trainer.controller.UserController")
@RequestMapping("/trainer/user")
public class UserController {
	
	@RequestMapping(method = RequestMethod.POST, path = "/")
	public ResponseEntity<?> save() {
		System.out.println("Gd");
		return ResponseEntity
				.ok()
				.body("");
	}
}
