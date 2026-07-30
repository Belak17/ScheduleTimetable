package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.request.UserRegister;
import com.belak.scheduletimetable.service.reset.PasswordResetService;
import com.belak.scheduletimetable.service.user.UserRegisterInterfaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class RegistrationController {
    private final PasswordResetService passwordResetService ;
    private final UserRegisterInterfaceService userRegisterService;

    @PostMapping("/api/register")
    public String register(@RequestBody UserRegister request) {

        return userRegisterService.register(request);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam String token)
    {
        userRegisterService.confirmToken(token);
        return "Votre compte a été activé";
    }





}
