package com.github.karsl.filmrepository.controller;

import com.github.karsl.filmrepository.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @GetMapping
  public String getLoginPage(Model model) {
    model.addAttribute("roles", loginService.getAllRoles());
    return "login";
  }

}
