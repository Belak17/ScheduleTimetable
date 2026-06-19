package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.PresenceValidationDto;
import com.belak.scheduletimetable.dto.StudentProfileDto;
import com.belak.scheduletimetable.enumeration.Semester;
import com.belak.scheduletimetable.model.Student;
import com.belak.scheduletimetable.response.LoginResponse;
import com.belak.scheduletimetable.service.presence.PresenceService;
import com.belak.scheduletimetable.service.student.StudentInterfaceService;
import com.belak.scheduletimetable.service.student.StudentPresenceInterfaceService;
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
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentInterfaceService studentService ;
    private final UserService userService ;
    private  final TimetablePreviewService preview ;
    private  final StudentPresenceInterfaceService studentPresenceService ;
    private  final PresenceService presenceService ;
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
    public ResponseEntity<byte[]> getPreview(Authentication authentication  , @RequestParam(required = false) Semester semester ) throws IOException {

        Semester effectiveSemester =
                (semester != null) ? semester : Semester.fromDate(LocalDate.now());
        byte[] image = preview
                .getTimetablePreview(authentication.getName(), effectiveSemester);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @GetMapping("/absences")
    public String getAllAbsencesByStudent(
            @RequestParam(required = false) Semester semester, Authentication authentication  , Model model)
    {
        Semester effectiveSemester =
                (semester != null) ? semester : Semester.fromDate(LocalDate.now());
         model.addAttribute("courses",studentPresenceService.getAllStudentOverviewByUserId(authentication.getName(),effectiveSemester));
        return "student/student-absence.html";
    }

    @GetMapping("/absences/{id}")
    public String getAllAbsencesByStudentAndCourses(Authentication authentication , Model model, @PathVariable long id, HttpSession session)
    {
        model.addAttribute("absencesList",studentPresenceService.getAllAbsenceByUserIdAndCoursTP(authentication.getName(),id));
        return "student/student-absences-courses.html";
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

    @PostMapping("/edit-my-profile/update/email")
    public String updateEmail( @RequestParam String nouvEmail, @RequestParam String confEmail ,
            @RequestParam(defaultValue = "student/change-email") String tab,
            Model model , Authentication authentication) {
        studentService.updateEmail(nouvEmail,confEmail, authentication.getName()) ;
        StudentProfileDto student = studentService.findByUserId(authentication.getName());
        model.addAttribute("student",student);
        model.addAttribute("fragmentName", tab);

        return "student/edit-my-profile";
    }

    @PostMapping("/edit-my-profile/update/info")
    public String updateInfo( @RequestParam String userId,
                              @RequestParam String address , @RequestParam String telephone ,
                              @RequestParam String code ,
                               @RequestParam(defaultValue = "student/change-info") String tab,
                               Model model , Authentication authentication) {
        studentService.updateInfo(userId,address,telephone,code);
        StudentProfileDto student = studentService.findByUserId(authentication.getName());
        model.addAttribute("student",student);
        model.addAttribute("fragmentName", tab);

        return "student/edit-my-profile";
    }


    @PostMapping("/edit-my-profile/update/password")
    public String updatePassword( @RequestParam String oldPassword,
                                  @RequestParam String newPassword,
                              @RequestParam String confPassword ,
                              @RequestParam(defaultValue = "student/change-password") String tab,
                              Model model , Authentication authentication) {
        studentService.updatePassword(oldPassword,newPassword,confPassword , authentication.getName()) ;
        StudentProfileDto student = studentService.findByUserId(authentication.getName());
        model.addAttribute("student",student);
        model.addAttribute("fragmentName", tab);

        return "student/edit-my-profile";
    }

    @GetMapping("/validation")
    public String validation(
            @RequestParam String intitule,
            @RequestParam String group,
            @RequestParam String code,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam String day,
            Model model
    ) {

        PresenceValidationDto validationDto = new PresenceValidationDto(intitule,group,code
        ,date,time,day);

        model.addAttribute("validation", validationDto);

        return "student/validation";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "student/errorPage";
    }

    @GetMapping("/alreadyRegistered")
    public String alreadyRegisteredPage() {
        return "student/alreadyRegistered";
    }

    @GetMapping("/codeNotFound")
    public String CodeErrorPage() {
        return "student/codeError";
    }



}
