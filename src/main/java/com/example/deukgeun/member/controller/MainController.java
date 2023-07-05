package com.example.deukgeun.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/my-page/jobPosting")
    public String jobPosting(Model model) {
        model.addAttribute("menu", "myPage");
        return "member/myPage/jobPosting";
    }
}
