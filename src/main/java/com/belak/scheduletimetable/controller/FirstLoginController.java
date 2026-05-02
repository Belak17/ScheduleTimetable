package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.service.reset.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@Controller
//@RequestMapping
//@RequiredArgsConstructor
//public class FirstLoginController {
//    private  final ForgotPasswordService forgotPasswordService ;
//    @GetMapping("/firstlogin")
//    public String showLogin()
//    {
//        return "login/firstlogin" ;
//    }
//
//    @PostMapping("/forgot-password")
//    public String processForgotPassword(@RequestParam String email, Model model) {
//        forgotPasswordService.sendResetLink(email);
//        model.addAttribute("message", "Si cet email existe, un lien a été envoyé.");
//        return "login/firstlogin";
//    }
//}
