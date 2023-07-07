package com.example.deukgeun.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
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

	@GetMapping("/jobPosting/{id}")
	public String JobPostingDetail(Model model, @PathVariable Long id) {
		model.addAttribute("menu", "main");
		return "jobPosting/detail";
	}
}
