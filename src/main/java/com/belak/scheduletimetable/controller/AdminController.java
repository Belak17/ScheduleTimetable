package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.admin.AdminService;
import com.belak.scheduletimetable.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private  final UserService userService ;
    @GetMapping("/dashboard")
    public String showAdminDashboard(Authentication authentication , Model model , RedirectAttributes redirectAttributes) {
         LoginResponse theResponse = userService.getUserData(authentication.getName());

            model.addAttribute("fullname",
                    theResponse.getNom()+" " + theResponse.getPrenom());
        return "admin/admin-dashboard.html";
    }
    @GetMapping("/profile")
    public String showAdminProfile(Authentication authentication ,  Model model ) {

        model.addAttribute("admin",adminService
                .findByUserId(authentication.getName()));
        return "admin/admin-profile.html";
    }
    @GetMapping("/timetable")
    public String showAdminUploadTimetable( Model model ) {
        return "admin/admin-upload-timetable.html";
    }
    @GetMapping("/import")
    public String showAdminImportUsers( Model model ) {
        return "admin/upload-users.html";
    }
    @GetMapping("/absences/department")
    public String showAdminAbsenceByDepartment( Model model ) {
        return "admin/show-department-absence.html";
    }

    @GetMapping("/edit-my-profile")
    public String profile(
            @RequestParam(defaultValue = "info") String tab,
            Model model , Authentication authentication) {

        String fragmentName = switch (tab) {
            case "photo" -> "admin/change-avatar";
            case "email" -> "admin/change-email";
            case "password" -> "admin/change-password";
            default -> "admin/change-info";
        };
        model.addAttribute("admin",adminService.findByUserId(authentication.getName()));
        model.addAttribute("fragmentName", fragmentName);

        return "admin/edit-my-profile";
    }
}
