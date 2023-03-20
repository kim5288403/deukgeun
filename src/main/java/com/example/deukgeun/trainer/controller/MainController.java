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
    return "/trainer/join";
  }
  
  @GetMapping("/my-page")
  public String trainerMyPage(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage";
  }
  
  @GetMapping("/my-page/info")
  public String myPageInfo(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/info";
  }
  
  @GetMapping("/my-page/profile")
  public String myPageProfile(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/profile";
  }
  
  @GetMapping("/my-page/password")
  public String myPagePassword(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/password";
  }
  
  @GetMapping("/my-page/withdrawal")
  public String myPageWithdrawal(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/withdrawal";
  }
}
