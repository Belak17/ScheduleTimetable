package com.belak.scheduletimetable.controller.login;

import com.belak.scheduletimetable.service.reset.PasswordResetService;
import lombok.RequiredArgsConstructor;
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

        model.addAttribute("message", passwordResetService.resetPassword(email));
        return "login/forgot" ;
    }

    @GetMapping("/reset/password")
    public String resetPassword( Model model , @RequestParam String token )
    {
        model.addAttribute("token", token);
        return "login/reset-password" ;
    }

    @PostMapping("/reset/password")
    public String resetFinalPassword(
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String token,
            Model model
    ) {
        // Vérifier que les deux mots de passe correspondent
        if (!password.trim().equals(confirmPassword.trim())) {
            model.addAttribute("error", "Les deux mots de passe ne correspondent pas.");
            model.addAttribute("token", token);
            return "login/reset-password";
        }
        model.addAttribute("message",passwordResetService.confirmResetToken(token, password.trim()));
        return "login/login" ;


    }

}
