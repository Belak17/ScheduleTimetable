package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.request.UserRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {
    @GetMapping("/forgot")
    public String showLogin( Model model)
    {
        return "login/forgot" ;
    }

}
