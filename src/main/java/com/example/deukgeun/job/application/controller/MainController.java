package com.example.deukgeun.job.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("job.controller.MainController")
public class MainController {

	@GetMapping("/jobPosting/{id}")
	public String JobPostingDetail(Model model, @PathVariable Long id) {
		model.addAttribute("menu", "main");
		return "jobPosting/detail";
	}
}
