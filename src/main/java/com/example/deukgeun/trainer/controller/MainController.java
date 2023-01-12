package com.example.deukgeun.trainer.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.deukgeun.trainer.service.MainService;


@RestController("trainer.controller.MainController")
@RequestMapping("/trainer")
public class MainController {
	
	@Autowired
	private MainService mainService;
	
	@RequestMapping("/")
	public ArrayList<?>  list() {
		ArrayList<?> list = mainService.getList(null);
		return list;
	}
	
}
