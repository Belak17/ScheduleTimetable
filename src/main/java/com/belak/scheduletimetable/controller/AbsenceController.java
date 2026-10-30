package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.dto.AttendanceDto;
import com.belak.scheduletimetable.dto.AttendancePageDto;
import com.belak.scheduletimetable.enumeration.Semester;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.repository.CoursTPRepository;
import com.belak.scheduletimetable.service.courstp.CoursTPService;
import com.belak.scheduletimetable.service.student.StudentPresenceInterfaceService;
import com.belak.scheduletimetable.service.student.StudentPresenceService;
import com.belak.scheduletimetable.service.timetable.grouptimetable.GroupTimetableService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/absence")
public class AbsenceController {
    private  final GroupTimetableService groupTimetableService ;
    private  final CoursTPService tpService ;
    private  final StudentPresenceInterfaceService studentPresenceService ;
    private  final CoursTPRepository coursTPrepository;
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
        return "admin/see-absence-final";
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
        return "admin/see-absence-final";
    }
    @GetMapping("/courses/{id}")
    public String showCoursePresence(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        size = 10;

        if (page < 0) {
            page = 0;
        }

        AttendancePageDto attendancePage =
                tpService.getAllDatesAndAttendanceByCoursTP(id, page, size);

        model.addAttribute("attendancePage", attendancePage);

        // On conserve le nom utilisé par ton template actuel
        model.addAttribute("attendanceTable", attendancePage.getAttendanceTable());

        model.addAttribute("currentPage", attendancePage.getCurrentPage());
        model.addAttribute("size", attendancePage.getPageSize());
        model.addAttribute("totalPages", attendancePage.getTotalPages());
        model.addAttribute("totalStudents", attendancePage.getTotalStudents());
        model.addAttribute("id", id);

        log.info("Nombre total étudiants : {}", attendancePage.getTotalStudents());
        log.info("Nombre de lignes AttendanceDto : {}",
                attendancePage.getAttendanceTable().getRows().size());
        log.info("Nombre de dates : {}",
                attendancePage.getAttendanceTable().getDates().size());

        return "admin/see-attendance-final";
    }
    @GetMapping("/{id}")
    public String showAllAbsencesByStudentToStudent(
            @RequestParam(required = false) Semester semester, Model model, @PathVariable String id, HttpSession session)
    {
        Semester effectiveSemester =
                (semester != null) ? semester : Semester.fromDate(LocalDate.now());
        model.addAttribute("courses",studentPresenceService.getAllStudentOverviewByUserId(id,effectiveSemester));
        return "student/student-absence.html";
    }

    @GetMapping("/admin/{id}")
    public String showAllAbsencesByStudentToAdmin(
            @RequestParam(required = false) Semester semester, Model model, @PathVariable String id, HttpSession session)
    {
        Semester effectiveSemester =
                (semester != null) ? semester : Semester.fromDate(LocalDate.now());
        model.addAttribute("courses",studentPresenceService.getAllStudentOverviewByUserId(id,effectiveSemester));
        return "student/student-absence.html";
    }
}
