package com.example.deukgeun.authToken.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("authToken.mainController")
public class MainController {
	@GetMapping("/")
	public String web(Model model) {
		model.addAttribute("menu", "main");

		return "web/main";
	}

	@GetMapping("/generic")
	public String generic(Model model) {
		model.addAttribute("menu", "main");
		return "/generic";
	}

	@GetMapping("/elements")
	public String elements(Model model) {
		model.addAttribute("menu", "main");
		return "/elements";
	}
	
	@GetMapping("/join")
	public String join(Model model) {
		model.addAttribute("menu", "join");
		return "web/join";
	}

	@GetMapping("/login")
    public String login(Model model) {
	  model.addAttribute("menu", "login");
	  return "web/login";
    }
}
