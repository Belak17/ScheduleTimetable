package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.*;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.professor.ProfessorService;
import com.belak.scheduletimetable.service.timetable.professortimetable.ProfessorPreviewService;
import com.belak.scheduletimetable.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorPreviewService professorPreviewService;
    private  final UserService userService ;
    private  final ProfessorService professorService ;
    @GetMapping("/dashboard")
    public String showProfessorDashboard( Model model
            , HttpSession session ,Authentication authentication)
    {
        LoginResponse theResponse = userService
                .getUserData(authentication.getName());
        model.addAttribute("fullname",
                theResponse.getNom()+" " + theResponse.getPrenom());
        return "professor/professor-app";
    }
    @GetMapping("/profile")
    public String showProfessorProfile( Model model , Authentication authentication
            ,HttpSession session )
    {
        ProfessorProfileDto professor = professorService.findByUserId(authentication.getName());
        model.addAttribute("professor",professor);
        return "professor/professor-profile";
    }
    @GetMapping("/timetable")
    public String showProfessorTimetable( Model model,
                                         HttpSession session )
    {

        return "professor/professor-timetable";
    }
    @GetMapping("/preview")
    public ResponseEntity<byte[]> getPreview(Authentication authentication) throws IOException {

        byte[] image = professorPreviewService
              .getTimetablePreview(authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }


}
