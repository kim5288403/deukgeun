package com.example.deukgeun.trainer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public String myPage(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage";
  }
  
  @GetMapping("/my-page/info")
  public String info(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/info";
  }
  
  @GetMapping("/my-page/profile")
  public String profile(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/profile";
  }
  
  @GetMapping("/my-page/password")
  public String password(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/password";
  }
  
  @GetMapping("/my-page/withdrawal")
  public String withdrawal(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/withdrawal";
  }
  
  @GetMapping("/my-page/post")
  public String post(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/post";
  }
  
  @GetMapping("/detail/{id}")
  public String detail(Model model,  @PathVariable Long id) {
    model.addAttribute("menu", "main");
    model.addAttribute("id", id);
    return "trainer/detail";
  }
  
  @GetMapping("/my-page/license")
  public String license(Model model) {
    model.addAttribute("menu", "myPage");
    return "trainer/myPage/license";
  }
}
