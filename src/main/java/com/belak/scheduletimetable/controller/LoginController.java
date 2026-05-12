package com.belak.scheduletimetable.controller;


import com.belak.scheduletimetable.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String loginPage(
            @RequestParam(required = false) String error,
            Model model
    ) {

        if (error != null) {
            model.addAttribute("error", error);
        }

        return "login/login";
    }
}
