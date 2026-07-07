package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.AdminProfileDto;
import com.belak.scheduletimetable.dto.ProfessorImportDto;
import com.belak.scheduletimetable.dto.StudentImportDto;
import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.*;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.admin.AdminInterfaceService;
import com.belak.scheduletimetable.service.admin.AdminService;
import com.belak.scheduletimetable.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminInterfaceService adminService;
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
    @GetMapping("/import/professors")
    public String showAdminImportProfessors( Model model ) {
        model.addAttribute("professor", new ProfessorImportDto());
        model.addAttribute("nationalites", Nationalite.values());
        model.addAttribute("grades", Grade.values());
        model.addAttribute("statuses", Statuts.values());
        model.addAttribute("departments", Departement.values());
        return "admin/upload-professors.html";
    }

    @GetMapping("/import/students")
    public String showAdminImportStudents( Model model )
    {
        model.addAttribute("student", new StudentImportDto());
        model.addAttribute("nationalites", Nationalite.values());
        model.addAttribute("filieres", Filiere.values());
        model.addAttribute("typeDiplome", TypeDiplome.values());
        model.addAttribute("departments", Departement.values());
        return "admin/upload-students.html";
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
        AdminProfileDto admin = adminService.findByUserId(authentication.getName());

        model.addAttribute("admin",admin);
        model.addAttribute("fragmentName", fragmentName);

        return "admin/edit-my-profile";
    }

    @PostMapping("/edit-my-profile/update/email")
    public String updateEmail( @RequestParam String nouvEmail, @RequestParam String confEmail ,
                               @RequestParam(defaultValue = "admin/change-email") String tab,
                               Model model , Authentication authentication) {
        adminService.updateEmail(nouvEmail,confEmail, authentication.getName()) ;
        AdminProfileDto admin = adminService.findByUserId(authentication.getName());
        model.addAttribute("admin",admin);
        model.addAttribute("fragmentName", tab);
        return "admin/edit-my-profile";
    }

    @PostMapping("/edit-my-profile/update/info")
    public String updateInfo( @RequestParam String userId,
                              @RequestParam String address , @RequestParam String telephone ,
                              @RequestParam String code ,
                              @RequestParam(defaultValue = "admin/change-info") String tab,
                              Model model , Authentication authentication) {
        adminService.updateInfo(userId,address,telephone,code);
        AdminProfileDto admin = adminService.findByUserId(authentication.getName());
        model.addAttribute("admin",admin);
        model.addAttribute("fragmentName", tab);

        return "admin/edit-my-profile";
    }


    @PostMapping("/edit-my-profile/update/password")
    public String updatePassword( @RequestParam String oldPassword,
                                  @RequestParam String newPassword,
                                  @RequestParam String confPassword ,
                                  @RequestParam(defaultValue = "admin/change-password") String tab,
                                  Model model , Authentication authentication) {
        adminService.updatePassword(oldPassword,newPassword,confPassword , authentication.getName()) ;
        AdminProfileDto admin = adminService.findByUserId(authentication.getName());
        model.addAttribute("admin",admin);
        model.addAttribute("fragmentName", tab);

        return "admin/edit-my-profile";
    }
}
