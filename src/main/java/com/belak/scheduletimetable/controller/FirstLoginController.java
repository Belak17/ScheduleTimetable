package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.request.UserRegister;
import com.belak.scheduletimetable.service.reset.ForgotPasswordService;
import com.belak.scheduletimetable.service.user.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class FirstLoginController {
    private  final ForgotPasswordService forgotPasswordService ;
    private  final UserRegisterService userRegisterService ;
    @GetMapping("/firstlogin")
    public String showLogin( Model model)
    {
        model.addAttribute("userRegister", new UserRegister());
        return "login/firstlogin" ;
    }


    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        forgotPasswordService.sendResetLink(email);
        model.addAttribute("message", "Si cet email existe, un lien a été envoyé.");
        return "login/firstlogin";
    }

    @PostMapping("/firstlogin")
    public  String register( Model model , @ModelAttribute UserRegister userRegister)
    {
        userRegisterService.registerUserByUserIdAndEmail(userRegister);
        model.addAttribute("message", "Votre compte a été activé ");
        return  "login/login" ;
    }
}
