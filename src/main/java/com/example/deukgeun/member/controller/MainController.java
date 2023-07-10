package com.example.deukgeun.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("member.controller.MainController")
@RequestMapping("/member")
public class MainController {
    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("menu", "join");
        return "/member/join";
    }

    @GetMapping("/my-page")
    public String myPage(Model model) {
        model.addAttribute("menu", "myPage");
        return "/member/myPage";
    }
    @GetMapping("/job")
    public String jobList(Model model) {
        model.addAttribute("menu", "myPage");
        return "member/job/list";
    }

    @GetMapping("/job/create")
    public String jobPosting(Model model) {
        model.addAttribute("menu", "myPage");
        return "member/job/create";
    }

    @GetMapping("/applicant/{id}")
    public String applicantList(Model model, @PathVariable Long id) {
        model.addAttribute("menu", "myPage");
        return "member/applicant/list";
    }
}
