package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.student.StudentPresenceService;
import com.belak.scheduletimetable.service.student.StudentService;
import com.belak.scheduletimetable.service.timetable.grouptimetable.TimetablePreviewService;
import com.belak.scheduletimetable.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService ;
    private final UserService userService ;
    private  final TimetablePreviewService preview ;
    private  final StudentPresenceService studentPresenceService ;
    @GetMapping("/dashboard")
    public String showStudentDashboard(Model model ,
                                       HttpSession session , Authentication authentication )
    {
        LoginResponse theResponse = userService.getUserData(authentication.getName());

        model.addAttribute("fullname",
                theResponse.getNom()+" " + theResponse.getPrenom());
        return "student/student-dashboard.html";
    }
    @GetMapping("/profile")
    public String showStudentProfile(Model model , Authentication authentication
            ,HttpSession session)
    {
        StudentProfileDto student = studentService.findByUserId(authentication.getName());
        model.addAttribute("student",student);
        return "student/student-profile.html";
    }
    @GetMapping("/timetable")
    public String showStudentTimetable(  Model model,
                                       HttpSession session)
    {
        return "student/student-timetable.html";
    }

    @GetMapping("/timetable/preview")
    public ResponseEntity<byte[]> getPreview(Authentication authentication) throws IOException {

        byte[] image = preview
                .getTimetablePreview(authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @GetMapping("/absences")
    public String getAllAbsencesByStudent(Authentication authentication  , Model model)
    {
         model.addAttribute("courses",studentPresenceService.getAllStudentOverviewByUserId(authentication.getName()));
        return "student/student-absence.html";
    }

    @GetMapping("/scanner")
    public String showScanner(Authentication authentication){
        return "student/scanner.html";
    }

    @GetMapping("/coursTP")
    public String showCoursTP(Authentication authentication)
    {
        return  "";
    }

    @GetMapping("/edit-my-profile")
    public String profile(
            @RequestParam(defaultValue = "info") String tab,
            Model model , Authentication authentication) {

        String fragmentName = switch (tab) {
            case "photo" -> "student/change-avatar";
            case "email" -> "student/change-email";
            case "password" -> "student/change-password";
            default -> "student/change-info";
        };
        StudentProfileDto student = studentService.findByUserId(authentication.getName());

        model.addAttribute("student",student);
        model.addAttribute("fragmentName", fragmentName);

        return "student/edit-my-profile";
    }
}
