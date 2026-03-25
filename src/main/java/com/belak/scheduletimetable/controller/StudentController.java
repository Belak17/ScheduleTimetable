package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.student.StudentService;
import com.belak.scheduletimetable.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    @GetMapping("/dashboard")
    public String showStudentDashboard(Model model ,
                                       HttpSession session , Authentication authentication )
    {
        LoginResponse theResponse = userService.getUserData(authentication.getName());

        model.addAttribute("fullname",
                theResponse.getNom()+" " + theResponse.getPrenom());
        return "student/student-dashboard";
    }
    @GetMapping("/profile")
    public String showStudentProfile(Model model
            ,HttpSession session)
    {
        return "student/student-profile";
    }
    @GetMapping("/timetable")
    public String showStudentTimetable(  Model model,
                                       HttpSession session)
    {
        return "student/student-timetable.html";
    }

    @GetMapping("/timetable/preview")
    public ResponseEntity<byte[]> getPreview(Authentication authentication) throws IOException {

        byte[] image = studentService
                .getTimetablePreview(authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }






}
