package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.enumeration.Semester;
import com.belak.scheduletimetable.service.courstp.CoursTPService;
import com.belak.scheduletimetable.service.student.StudentPresenceInterfaceService;
import com.belak.scheduletimetable.service.student.StudentPresenceService;
import com.belak.scheduletimetable.service.timetable.grouptimetable.GroupTimetableService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/absence")
public class AbsenceController {
    private  final GroupTimetableService groupTimetableService ;
    private  final CoursTPService tpService ;
    private  final StudentPresenceInterfaceService studentPresenceService ;
    @GetMapping("/{departement}/{filiere}/{niveau}")
    public String showGroup(
            @PathVariable String departement,
            @PathVariable String filiere,
            @PathVariable int niveau ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        model.addAttribute("courses", null);
        model.addAttribute("selectedDepartment", departement);
        model.addAttribute("selectedField",filiere);
        model.addAttribute("selectedNiveau",niveau);
        model.addAttribute("groups",groupTimetableService.getAllGroupByDepartmentAndFieldAndLevel(departement,filiere,niveau));
        return "/admin/see-absence-final";
    }
    @GetMapping("/{departement}/{filiere}/{niveau}/{group}")
    public String showGroupGeneral(
            @PathVariable String departement ,
            @PathVariable String filiere ,
            @PathVariable String group ,
            @PathVariable int niveau ,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        model.addAttribute("selectedDepartment", departement);
        model.addAttribute("selectedField",filiere);
        model.addAttribute("selectedNiveau",niveau);
        model.addAttribute("selectedGroup", group);
        model.addAttribute("courses", tpService.getAllCoursTPByGroupTimetable(page,size,departement,group,niveau,filiere));
        model.addAttribute("groups",groupTimetableService.getAllGroupByDepartmentAndFieldAndLevel(departement,filiere,niveau));
        return "/admin/see-absence-final";
    }
    @GetMapping("/courses/{id}")
    public String showCoursePresence(@PathVariable Long id,@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,  Model model)
    {
        model.addAttribute("attendanceTable",tpService.getAllDatesAndAttendanceByCoursTP(id,page,size));
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        return "/admin/see-attendance-final";
    }
    @GetMapping("/{id}")
    public String getAllAbsencesByStudent(
            @RequestParam(required = false) Semester semester, Model model, @PathVariable String id, HttpSession session)
    {
        Semester effectiveSemester =
                (semester != null) ? semester : Semester.fromDate(LocalDate.now());
        model.addAttribute("courses",studentPresenceService.getAllStudentOverviewByUserId(id,effectiveSemester));
        return "student/student-absence.html";
    }
}
