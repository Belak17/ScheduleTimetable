package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.PasswordResetToken;
import com.belak.scheduletimetable.service.reset.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller("/reset-password")
@RequiredArgsConstructor
public class ResetPasswordController {
    private  final PasswordResetService passwordResetService ;

    @GetMapping
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "login/reset-password";
    }

    @PostMapping
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            Model model) {

        try {
            passwordResetService.resetPassword(token,newPassword);
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "login/reset-password";
        }
        model.addAttribute("message", "Mot de passe mis à jour !");
        return "login/login";
    }
}
