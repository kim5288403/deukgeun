package com.example.deukgeun.trainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("trainer.controller.MainController")
@RequestMapping("/trainer")
public class MainController {
	
	@GetMapping("/join")
	public String join(Model model) {
		model.addAttribute("menu", "join");
		return "tainer/join";
	}
}
