package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.service.reset.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class ForgotPasswordController {
    private  final PasswordResetService passwordResetService;
    @GetMapping("/forgot")
    public String showLogin( Model model)
    {
        return "login/forgot" ;
    }

    @PostMapping("/forgot")
    public String resetPasswordLink( Model model , @RequestParam String email )
    {
        passwordResetService.resetPassword(email);
        return "login/forgot" ;
    }

    @GetMapping("/reset/password")
    public String resetPassword( Model model , @RequestParam String token )
    {
        model.addAttribute("token", token);
        return "login/reset-password" ;
    }

    @PostMapping("/reset/password")
    public String resetFinalPassword(@RequestParam String password,
                                     @RequestParam String token , Model model ) {
        System.out.println("Token reçu : " + token);
         model.addAttribute("message",passwordResetService.confirmResetToken(token, password));
         return "login/login" ;
    }

}
