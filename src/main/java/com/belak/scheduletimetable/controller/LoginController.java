package com.belak.scheduletimetable.controller;


import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.request.LoginRequest;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
//    @PostMapping
//    public String login(@ModelAttribute LoginRequest loginRequest ,
//                        RedirectAttributes redirectAttributes) {
//        System.out.println(loginRequest.getUserId() + " " + loginRequest.getPassword());
//
//        try {
//            LoginResponse theResponse  = loginService.findRole(loginRequest);
//
//            if (theResponse.getRole().equals("ADMIN")) {
//                redirectAttributes.addAttribute("id", theResponse.getId());
//                return "redirect:/admin/dashboard";}
//            else if (theResponse.getRole().equals("STUDENT")) {
//                redirectAttributes.addAttribute("id", theResponse.getId());
//                return "redirect:/student/dashboard";}
//            else if (theResponse.getRole().equals("PROFESSOR")) {
//                redirectAttributes.addAttribute("id", theResponse.getId());
//                return "redirect:/professor/dashboard";}
//        } catch (ResourceNotFoundException e) {
//            redirectAttributes.addFlashAttribute("failure", e.getMessage());
//            return "redirect:/login";
//        }
//        return "";
//    }
//
}
