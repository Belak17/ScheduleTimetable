package com.belak.scheduletimetable.controller;


import com.belak.scheduletimetable.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService ;

    @GetMapping
    public String showLogin()
    {
        return "login/login" ;
    }

}
