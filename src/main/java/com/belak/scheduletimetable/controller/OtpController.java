package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.service.user.UserRegisterInterfaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class OtpController {
    private  final UserRegisterInterfaceService userRegisterService;
    @PostMapping("/verify/otp")
    public  String verifyOtp(@RequestParam int otp, Model model, RedirectAttributes redirectAttributes)
    {

        log.info("Début vérification OTP");

        String message = userRegisterService.confirmToken(otp);

        log.info("Résultat OTP : {}", message);
        redirectAttributes.addFlashAttribute("message",message);
        return  "redirect:/login";
    }
}
