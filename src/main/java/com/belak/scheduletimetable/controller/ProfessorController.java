package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorTimetableDto;
import com.belak.scheduletimetable.dto.UserDto;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.professor.ProfessorService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService ;
    private  final UserService userService ;
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
    public String showProfessorProfile( Model model
            ,HttpSession session )
    {
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

        byte[] image = professorService
              .getTimetablePreview(authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }


}
